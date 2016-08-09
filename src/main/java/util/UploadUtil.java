package util;

import java.io.File;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;


@SuppressWarnings("all")
public class UploadUtil {
	
	public static void uploadCloud(File file, String fileName) {
		uploadCloud(file, fileName, PropertiesUtil.getStringValue("PIC_BUCKET"));
	}
	/**
	 * 上传文件到云储存
	 * @param fileName
	 */
	public static void uploadCloud(File file, String fileName, String bucketName) {
		Auth auth = Auth.create(PropertiesUtil.getStringValue("ACCESS_KEY"),PropertiesUtil.getStringValue("SKEY"));
		String uploadToken = auth.uploadToken(bucketName,fileName);
		UploadManager uploadManager = new UploadManager();
		try {
			uploadManager.put(file, fileName, uploadToken);
			System.out.println("上传完成");
		} catch (QiniuException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String args[]){
		File file = new File("C:/Users/BFD_225/Documents/Tencent Files/314983474/FileRecv/logo.png");
		uploadCloud(file,"logo.png");
	}
}
