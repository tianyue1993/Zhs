package com.zhs.zhs.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhs.zhs.R;
import com.zhs.zhs.utils.GetPathFromUri4kitkat;
import com.zhs.zhs.utils.StringUtils;
import com.zhs.zhs.utils.ZhsUtils;
import com.zhs.zhs.views.CircleImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonNewsSetActivity extends Baseactivity {

    @Bind(R.id.personalavator_ciriv)
    CircleImageView avator;
    @Bind(R.id.personalavator_rl)
    RelativeLayout personalavatorRl;
    @Bind(R.id.personal_username_tv)
    TextView personalUsernameTv;
    @Bind(R.id.personal_username_rl)
    RelativeLayout personalUsernameRl;
    @Bind(R.id.personal_phone_tv)
    TextView personalPhoneTv;
    @Bind(R.id.personal_phone_rl)
    RelativeLayout personalPhoneRl;
    @Bind(R.id.activity_person_news_set)
    RelativeLayout activityPersonNewsSet;

    /**
     * 头像文件
     */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    public static final int PIC = 1;
    public static final int TAKEPHOTO = 11;
    public static final int PICKPHOTO = 12;
    public static final int PICK_PHOTO_FROM_MEIZU = 16;
    private static final int TAKE_PHOTO = 10;

    protected Bitmap photo;
    protected File mCurrentPhotoFile;

    private Uri photoUri;
    private File mUriFile;
    private static final int CAMERA_PIC = 21;
    private static final int CROP_PIC = 22;
    private static final int CODE_CAMERA_REQUEST = 30;
    private static final int CODE_RESULT_REQUEST = 33;
    private static final int RENAME = 38;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_news_set);
        ButterKnife.bind(this);
        setTitleTextView("个人信息", null);
        if (StringUtils.isNotEmptyOrNull(prefs.getCustomName())) {
            personalUsernameTv.setText(prefs.getCustomName());
        } else {
            personalUsernameTv.setText("请设置昵称");
        }

        if (!StringUtils.isEmpty(prefs.getUserPhone())) {
            personalPhoneTv.setText(prefs.getUserPhone());
        } else {
            personalPhoneTv.setText("请设置手机号");
        }


    }

    @OnClick({R.id.personalavator_rl, R.id.personal_username_rl, R.id.personal_phone_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.personalavator_rl:
                personalavator_rl();
                break;
            case R.id.personal_username_rl:
                Intent data = new Intent(mContext, RenameActivity.class);
                startActivityForResult(data, RENAME);
                break;
            case R.id.personal_phone_rl:
                Intent intent = new Intent(mContext, ResetPhoneActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;

        }
    }

    // 头像设置
    void personalavator_rl() {
        Intent intent = new Intent(mContext, SelectPicPopupWindowActivity.class);
        startActivityForResult(intent, PIC);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        } else {
            switch (requestCode) {
                case TAKE_PHOTO:
                    String originalPath = null;
                    Uri uri = null;
                    if (data != null && data.getData() != null) {
                        uri = data.getData();
                    }
                    // 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri (魅族无法获取到URI)
                    if (uri == null) {
                        if (photoUri != null) {
                            uri = photoUri;
                        }
                    }
                    // 头像正常情况 下，需要剪裁
                    tryCropProfileImage(uri);
                    break;
                default:
                    break;
            }

        }
        if (data != null) {
            switch (requestCode) {
                case PIC:
                    String pic = data.getStringExtra("picmethod");
                    if (pic.equals("TAKEPHOTO")) {
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            Intent takeintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            System.gc();
                            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMM_dd_HH_mm_ss");
                            String filename = timeStampFormat.format(new Date());
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, filename);
                            photoUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            takeintent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                            startActivityForResult(takeintent, TAKE_PHOTO);
                        } else {
                            Toast.makeText(mContext, "没有SD卡", Toast.LENGTH_SHORT).show();
                        }
                    } else if (pic.equals("PICKPHOTO")) {
                        doPickPhoto();
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    if (data != null) {
                        setImageToHeadView(data);
                    }
                    break;
                case CODE_CAMERA_REQUEST:
                    if (hasSdcard()) {
                        File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                        cropRawPhoto(Uri.fromFile(tempFile));
                    } else {
                        Toast.makeText(mContext, "没有SDCard!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PICK_PHOTO_FROM_MEIZU:
                    photo = data.getParcelableExtra("data");
                    if (photo == null) {
                        Uri originalUri;
                        if (data.getData() == null) {
                            String filePath = data.getStringExtra("filePath");
                            originalUri = Uri.fromFile(new File(filePath));
                        } else {
                            originalUri = data.getData();
                        }
                        tryCropProfileImage(originalUri);
                    } else {
                        mCurrentPhotoFile = new File(ZhsUtils.getTmpCachePath() + "screenshot.png");
                        try {
                            saveBitmap(mCurrentPhotoFile.getAbsolutePath(), photo);

                            onHeaderSelectedCallBack(photo);
                            updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case PICKPHOTO:
                    if (mCurrentPhotoFile != null && mCurrentPhotoFile.exists()) {
                        mCurrentPhotoFile.delete();
                    }

                    photo = data.getParcelableExtra("data");
                    if (photo == null) {
                        // get photo url
                        Uri originalUri;
                        if (data.getData() == null) {
                            String filePath = data.getStringExtra("filePath");
                            originalUri = Uri.fromFile(new File(filePath));
                        } else {
                            originalUri = data.getData();
                        }
                        try {
                            onLicenseSelectedCallBack(originalUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        mCurrentPhotoFile = new File(ZhsUtils.getTmpCachePath() + "screenshot.png");
                        try {
                            saveBitmap(mCurrentPhotoFile.getAbsolutePath(), photo);
                            onHeaderSelectedCallBack(photo);
                            updateAvatorByFile(mCurrentPhotoFile.getAbsolutePath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case TAKE_PHOTO:
                    if (resultCode == RESULT_OK) {
                        String originalPath = null;
                        Uri uri = null;
                        if (data != null && data.getData() != null) {
                            uri = data.getData();
                        }
                        // 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
                        if (uri == null) {
                            if (photoUri != null) {
                                uri = photoUri;
                            }
                        }
                        originalPath = GetPathFromUri4kitkat.getPath(mContext, uri);
                        originalPath = uri2filePath(uri);
                        if (StringUtils.isEmpty(originalPath)) {
                            updateAvatorByFile(originalPath);
                        } else {
                            Toast.makeText(mContext, "拍照失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case TAKEPHOTO:
                    Uri originalUri = data.getData();
                    tryCropProfileImage(Uri.fromFile(mCurrentPhotoFile));
                    break;
                case CAMERA_PIC:
                    cropPhoto(Uri.fromFile(mUriFile));
                    break;
                case RENAME:
                    if (data.getStringExtra("name") != null) {
                        personalUsernameTv.setText(data.getStringExtra("name"));
                    }
                    break;
            }
        }
    }


    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
        }
    }

    protected void onLicenseSelectedCallBack(Uri url) {
        avator.setImageURI(url);
        updateAvatorByUrl(url);
    }


    /**
     * 裁剪原始的图片
     */
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    private void updateAvatorByUrl(Uri url) {
        updateAvatorByFile(getAbsoluteImagePath(url));
    }

    private void updateAvatorByFile(String absolutePath) {
        if (!StringUtils.isEmpty(absolutePath)) {
            if (new File(absolutePath).isFile()) {
//                Log.e(tag, "startUpload PIC File : " + absolutePath);
//                RequestParams params = new RequestParams();
//                params.put("access_token", prefs.getAccessToken());
//                try {
//                    params.put("avatar", new File(absolutePath));
//                } catch (FileNotFoundException e1) {
//                    Log.e(tag, "文件没有找到");
//                    e1.printStackTrace();
//                    return;
//                }
//                cancelmDialog();
//                showProgress(0, true);
//                client.toUploadUserAvator(this, params, new CommenHandler() {
//
//                    @Override
//                    public void onCancel() {
//                        super.onCancel();
//                        cancelmDialog();
//                    }
//
//                    @Override
//                    public void onSuccess(Commen commen) {
//                        super.onSuccess(commen);
//                        cancelmDialog();
//                        Toast.makeText(mContext, commen.message, Toast.LENGTH_SHORT).show();
//                        prefs.setAvatar(commen.content);
//                        prefs.saveLoginUserAvatar(commen.content);
//
//                    }
//
//                    @Override
//                    public void onFailure(BaseException exception) {
//                        super.onFailure(exception);
//                        cancelmDialog();
//                    }
//
//                });
            } else {
                Toast.makeText(mContext, "文件路径出错，上传头像失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 把Uri转化成文件路径
     */
    private String uri2filePath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(index);
        cursor.close();
        return path;
    }

    protected String getAbsoluteImagePath(final Uri uri) {
        // can post image
        final String[] proj = {MediaStore.Images.Media.DATA};
        String path = GetPathFromUri4kitkat.getPath(mContext, uri);
        if (!StringUtils.isEmpty(path)) {
            return path;
        }
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, proj, // Which columns to return
                null, // WHERE clause; which rows to return (all rows)
                null, // WHERE clause selection arguments (none)
                null); // Order-by clause (ascending by name)
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected void onHeaderSelectedCallBack(Bitmap bp) {
        if (bp != null) {
            avator.setImageBitmap(bp);
        }

    }

    private void tryCropProfileImage(Uri uri) {
        try {
            // start gallery to crop photo
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void saveBitmap(String filePath, Bitmap bitmap) throws IOException {
        if (bitmap != null) {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap = compressImage(bitmap);
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);// 图片设置为压缩一半
            // 11.24 18：50
            out.flush();
            out.close();
        }
    }

    protected Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    //图片操作
    private void doPickPhoto() {
        if (Build.BRAND.equalsIgnoreCase("Meizu")) {
            Intent itentFromGallery = new Intent();
            itentFromGallery.setType("image/*");
            itentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(itentFromGallery, PICK_PHOTO_FROM_MEIZU);
            return;
        }

        try {
            // Launch picker to choose photo for selected contact
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(Uri.parse("content://media/internal/images/media"));
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("return-data", true);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }


    private boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }


    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CROP_PIC);
    }
}
