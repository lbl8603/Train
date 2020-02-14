package com.wherewego.train.service.impl;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.ocr.v20181119.OcrClient;
import com.tencentcloudapi.ocr.v20181119.models.TrainTicketOCRRequest;
import com.tencentcloudapi.ocr.v20181119.models.TrainTicketOCRResponse;
import com.wherewego.train.constant.ErrorCode;
import com.wherewego.train.dao.TrainTicketInfoRepository;
import com.wherewego.train.entity.TrainTicketInfo;
import com.wherewego.train.exception.WhereWeGoException;
import com.wherewego.train.service.TrainTicketService;
import com.wherewego.train.utils.MD5Util;
import com.wherewego.train.utils.OnlineUserManger;
import com.wherewego.train.utils.QRCodeUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JavaIdentifierTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Timestamp;
import java.util.*;

import static com.wherewego.train.constant.WhereWeGoSystem.ROOT_PATH;

/**
 * @Author:lubeilin
 * @Date:Created in 11:24 2020/2/10
 * @Modified By:
 */
@Service
public class TrainTicketServiceImpl implements TrainTicketService{
    private static Logger logger = LoggerFactory.getLogger(TrainTicketService.class);
    @Value("${qr-code-url}")
    private String url;
    @Autowired
    private TrainTicketInfoRepository repository;
    @Override
    public String createTrainTicketQRCodeImage(String ticketCode,String content){
        try {
            String userCode = OnlineUserManger.getUser().getUserCode();
            BufferedImage image = QRCodeUtils.createQRCodeImage(content,200,200,"JPG");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("user_file/").append(userCode).append("/QRCode/").append(ticketCode).append('-')
                    .append(new Random().nextInt(10000)).append(".jpg");
            String path = stringBuilder.toString();
            File file = new File(ROOT_PATH+path);
            if (!file.exists()) {
                file.mkdirs();
            }
            ImageIO.write(image,"JPG",file);
            return "/"+path;
        }catch (Exception e){
            logger.info("生成二维码失败");
            throw new WhereWeGoException(ErrorCode.CreateQRCodeError.getCode(),ErrorCode.CreateQRCodeError.getMsg());
        }


    }
    @Override
    public TrainTicketInfo ocrTrainTicket(String imageBase64){
        if (imageBase64==null||imageBase64.equals("")){
            throw new WhereWeGoException(ErrorCode.ParamError.getCode(),ErrorCode.ParamError.getMsg());
        }
        try{

            Credential cred = new Credential("AKIDoZAaml4HpENM9jTJhtuiIvMayDG4MUIu", "ZLr9BzsfEYgldGgwWv8A3jz3av1vdBWb");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            OcrClient client = new OcrClient(cred, "ap-guangzhou", clientProfile);

            String params = "{\"ImageBase64\":\""+imageBase64+"\"}";
            TrainTicketOCRRequest req = TrainTicketOCRRequest.fromJsonString(params, TrainTicketOCRRequest.class);

            TrainTicketOCRResponse resp = client.TrainTicketOCR(req);

            String result = TrainTicketOCRRequest.toJsonString(resp);

            JSONObject jsonObject = JSONObject.fromObject(result);
            JsonConfig config = new JsonConfig();
            config.setJavaIdentifierTransformer(new JavaIdentifierTransformer() {
                @Override
                public String transformToJavaIdentifier(String s) {
                    char[] chars = s.toCharArray();
                    chars[0] = Character.toLowerCase(chars[0]);
                    return new String(chars);
                }
            });
            config.setRootClass(TrainTicketInfo.class);
            TrainTicketInfo trainTicketInfo = (TrainTicketInfo)JSONObject.toBean(jsonObject,config);
            if(trainTicketInfo.getStartStation()==null||"".equals(trainTicketInfo.getStartStation())
                    ||trainTicketInfo.getDestinationStation()==null||"".equals(trainTicketInfo.getDestinationStation())){
                throw new WhereWeGoException(ErrorCode.OcrFailed.getCode(),ErrorCode.OcrFailed.getMsg());
            }
            return trainTicketInfo;
        } catch (TencentCloudSDKException e) {
            logger.info(e.getMessage());
            String err = e.getMessage().split("-")[0];
            switch (err){
                case "FailedOperation.DownLoadError":
                    throw new WhereWeGoException(ErrorCode.DownLoadError.getCode(),ErrorCode.DownLoadError.getMsg());
                case "LimitExceeded.TooLargeFileError":
                    throw new WhereWeGoException(ErrorCode.TooLargeFileError.getCode(),ErrorCode.TooLargeFileError.getMsg());
                default:
                    throw new WhereWeGoException(ErrorCode.OcrFailed.getCode(),ErrorCode.OcrFailed.getMsg());
            }

        }
    }

    @Override
    public TrainTicketInfo ocrTrainTicketImgFrame(Integer[] array, int width, int height) {
        try {
            BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            image.setRGB(0, 0, width, height, Arrays.stream(array).mapToInt(Integer::intValue).toArray(), 0, width);
            BASE64Encoder base64Encoder =new BASE64Encoder();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( image, "JPG", baos );
            baos.flush();
            //使用toByteArray()方法转换成字节数组
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            String base64EncoderImg =base64Encoder.encode(imageInByte);
            TrainTicketInfo ticketInfo = ocrTrainTicket(base64EncoderImg);
            return ticketInfo;
        }catch (WhereWeGoException e){
            logger.info(e.getMessage(),e);
            throw new WhereWeGoException(e.getCode(),e.getMessage());
        }catch (Exception e){
            logger.error("图片转换出错",e);
            throw new WhereWeGoException(ErrorCode.UnKnowError.getCode(),ErrorCode.UnKnowError.getMsg());
        }

//        try {
//            int i = new Random().nextInt(1000);
//            ImageIO.write(image,"JPG",new File("C:\\wherewego_new\\user_file\\111\\QRCode\\xx"+i+".jpg"));
//        }catch (Exception e){
//
//        }
//        return ticketInfo;
    }

    @Override
    public TrainTicketInfo saveTrainTicket(TrainTicketInfo ticketInfo) {
        try {
            ticketInfo.setUserCode(OnlineUserManger.getUser().getUserCode());
            ticketInfo.setTicketCode(UUID.randomUUID().toString().replace("-",""));
            ticketInfo.setCreateTime(new Timestamp(System.currentTimeMillis()));
            ticketInfo.setModifyTime(ticketInfo.getCreateTime());
            if(ticketInfo.getUserCode()==null){
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder(url);
            stringBuilder.append("?code=").append(ticketInfo.getTicketCode())
                    .append("&sign=").append(MD5Util.getMD5(ticketInfo.getTicketCode()));
            ticketInfo.setQrCodePath(this.createTrainTicketQRCodeImage(ticketInfo.getTicketCode(),stringBuilder.toString()));
            return repository.save(ticketInfo);
        }catch (Exception e){
            return null;
        }
    }

    @Override
    @Transactional
    public int deleteTrainTicket(String ticketCode) {
        String userCode = OnlineUserManger.getUser().getUserCode();
        TrainTicketInfo trainTicketInfo = this.repository.getTrainTicketInfoByTicketCode(ticketCode);
        if(!userCode.equals(trainTicketInfo.getUserCode())){
            throw new WhereWeGoException(ErrorCode.ParamError.getCode(),"参数错误");
        }
        File file = new File(trainTicketInfo.getQrCodePath());
        if(file.exists()){
            file.delete();
        }
        try {
            return repository.deleteTrainTicketInfoByTicketCodeAndUserCode(ticketCode,userCode);
        }catch (Exception e){
            logger.info(e.getMessage(),e);
            throw new WhereWeGoException(ErrorCode.ParamError.getCode(),"删除失败");
        }
    }

    @Override
    public Page<TrainTicketInfo> findList(int page, int size) {
        final String userCode = OnlineUserManger.getUser().getUserCode();
        try {
            Sort.Order order = new Sort.Order(Sort.Direction.DESC, "date");
            Sort.Order order1 = new Sort.Order(Sort.Direction.DESC, "createTime");
            Sort sort = Sort.by(order,order1);
//            Page<TrainTicketInfo> infoPage = repository.findByUserCode(userCode,PageRequest.of(page,size,sort));
//            return infoPage;
            //规格定义
            Specification<TrainTicketInfo> specification = new Specification<TrainTicketInfo>() {

                /**
                 * 构造断言
                 * @param root 实体对象引用
                 * @param query 规则查询对象
                 * @param cb 规则构建对象
                 * @return 断言
                 */
                @Override
                public Predicate toPredicate(Root<TrainTicketInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    List<Predicate> predicates = new ArrayList<>(); //所有的断言
                    Predicate likeNickName = cb.equal(root.get("userCode").as(String.class),userCode);
                    predicates.add(likeNickName);
                    return cb.and(predicates.toArray(new Predicate[0]));
                }
            };
            //分页信息
            Pageable pageable = PageRequest.of(page,size,sort); //页码：前端从1开始，jpa从0开始，做个转换
            //查询
            return this.repository.findAll(specification,pageable);
        }catch (Exception e){
            logger.debug(e.getMessage(),e);
            return new PageImpl<>(new ArrayList<>());
        }

    }

    @Override
    public TrainTicketInfo scanQRCode(String code, String sign) {
        if(StringUtils.isEmpty(code)||StringUtils.isEmpty(sign)){
            throw new WhereWeGoException(ErrorCode.ParamError.getCode(),ErrorCode.ParamError.getMsg());
        }
        String codeSign = MD5Util.getMD5(code);
        if(!codeSign.equals(sign)){
            throw new WhereWeGoException(ErrorCode.ParamError.getCode(),ErrorCode.ParamError.getMsg());
        }
        try {
            TrainTicketInfo trainTicketInfo = repository.getTrainTicketInfoByTicketCode(code);
            return trainTicketInfo;
        }catch (Exception e){
            return null;
        }
    }
}
