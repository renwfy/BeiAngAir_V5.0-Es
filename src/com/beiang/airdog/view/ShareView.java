package com.beiang.airdog.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;
import com.beiang.airdog.utils.LogUtil;

public class ShareView {
	Context context;
	private FrontiaSocialShare mSocialShare;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();

	public ShareView(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		initShareConfig(context);
	}

	private void initShareConfig(Context context) {
		mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(context);
		mSocialShare.setClientId(MediaType.SINAWEIBO.toString(), "2112656390");
		mSocialShare.setClientId(MediaType.QZONE.toString(), "1102154446");
		mSocialShare.setClientId(MediaType.QQFRIEND.toString(), "1102154446");
		mSocialShare.setClientName(MediaType.QQFRIEND.toString(), "贝昂");
		mSocialShare.setClientId(MediaType.WEIXIN.toString(), "wxc8f3dbe24ba8e504");

		mImageContent.setQQRequestType(FrontiaSocialShareContent.FrontiaIQQReqestType.TYPE_IMAGE);
		mImageContent.setWXMediaObjectType(FrontiaSocialShareContent.FrontiaIMediaObject.TYPE_IMAGE);
		mImageContent.setTitle("贝昂空气净化器");
		mImageContent.setContent("欢迎使贝昂空气净化器，实时监控你的空气质量！");
		mImageContent.setLinkUrl(" ");
	}

	public void show(Bitmap bitmap) {
		if (bitmap != null) {
			mImageContent.setImageData(bitmap);
		}
		mSocialShare.show(((Activity) context).getWindow().getDecorView(), mImageContent, FrontiaTheme.LIGHT, new ShareListener());
	}

	private class ShareListener implements FrontiaSocialShareListener {
		@Override
		public void onSuccess() {
			LogUtil.d("Test", "share success");
			Toast.makeText(context, "分享成功!", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			LogUtil.d("Test", "share errCode " + errCode);
			Toast.makeText(context, "分享失败!", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel() {
			LogUtil.d("Test", "cancel ");
			Toast.makeText(context, "分享取消!", Toast.LENGTH_SHORT).show();
		}

	}

}
