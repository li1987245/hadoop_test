package util;

import javax.mail.*;
import javax.mail.internet.*;


public class MailUtil {
	private MimeMessage mimeMsg; // MIME邮件对象
	private Session session; // 邮件会话对象
	private Multipart mp; 
	/**
	 * 
	 * @param subject 标题
	 * @param body 内容
	 * @param sendTo 收件人邮箱列表
	 */
	public MailUtil(String subject, String body, String sendTo) {
		try {
			session = Session.getDefaultInstance(PropertiesUtil.getProperties(), null); // 获得邮件会话对象
		} catch (Exception e) {
			System.out.println("获取邮件会话对象时发生错误!" + e);
		}
		try {
			mimeMsg = new MimeMessage(session); // 创建MIME邮件对象
			mp = new MimeMultipart();
		} catch (Exception e) {
			System.out.println("创建MIME邮件对象失败!" + e);
		}
		try {
			mimeMsg.setSubject(subject);
		} catch (Exception e) {
			System.out.println("设置邮件主题发生错误!");
		}
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("" + body, PropertiesUtil.getStringValue("APP_SERVER_EMAIL_CONTENT_TYPE"));
			mp.addBodyPart(bp);
		} catch (Exception e) {
			System.out.println("设置邮件正文时发生错误!" + e);
		}
		try {
			mimeMsg.setFrom(new InternetAddress(PropertiesUtil.getStringValue("APP_SERVER_EMAIL_FROM"))); // 设置发信人
		} catch (Exception e) {
			System.out.println("设置发件人发生错误!");
		}
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(sendTo));
		} catch (Exception e) {
			System.out.println("设置发件人发生错误!" + e);
		}
	}
	
	public void sendout() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			Session mailSession = Session.getInstance(PropertiesUtil.getProperties(), null);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect(PropertiesUtil.getStringValue("APP_SERVER_EMAIL_STMP"), PropertiesUtil.getStringValue("APP_SERVER_EMAIL_NAME"),PropertiesUtil.getStringValue("APP_SERVER_EMAIL_PWD"));
			transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO));
			transport.close();
		} catch (Exception e) {
			System.out.println("邮件发送失败!" + e);
		}
	}


	
	public static void main(String[] args) {
//		TemplateDataModel model = new TemplateDataModel();
//		model.setFromUserName("DataTestApi");
//		getDoc("data",model);
//		File file = new File(emailFilePath);
//		String str = getStringFromFile(file,"UTF-8");
//		MailUtil mail = new MailUtil("会议app测试",str,"guang.chen@baifendian.com");
//		mail.sendout();
//		System.out.println(templateDir);
		System.out.println(System.currentTimeMillis());
	}
}