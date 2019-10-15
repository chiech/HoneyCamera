package com.xinlan.imageeditlibrary.editimage.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;


/**
 * 工具栏主菜单
 *
 * @author panyi
 */
public class MainMenuFragment extends BaseEditFragment implements View.OnClickListener {
    public static final int INDEX = ModuleConfig.INDEX_MAIN;

    public static final String TAG = MainMenuFragment.class.getName();
    private View mainView;

    private View stickerBtn;// 贴图按钮
    //    private View fliterBtn;// 滤镜按钮
    private View cropBtn;// 剪裁按钮
    private View rotateBtn;// 旋转按钮
    private View mTextBtn;//文字型贴图添加
    private View mPaintBtn;//编辑按钮
    private View mBeautyBtn;//美颜按钮

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_main_menu,
                null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        stickerBtn = mainView.findViewById(R.id.ll_sticker);
//        fliterBtn = mainView.findViewById(R.id.btn_filter);
        cropBtn = mainView.findViewById(R.id.ll_crop);
        rotateBtn = mainView.findViewById(R.id.ll_rotate);
        mTextBtn = mainView.findViewById(R.id.ll_text);
        mPaintBtn = mainView.findViewById(R.id.ll_paint);
        mBeautyBtn = mainView.findViewById(R.id.ll_beauty);

        stickerBtn.setOnClickListener(this);
//        fliterBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
        mTextBtn.setOnClickListener(this);
        mPaintBtn.setOnClickListener(this);
        mBeautyBtn.setOnClickListener(this);
    }

    @Override
    public void onShow() {
        // do nothing
    }

    @Override
    public void backToMain() {
        //do nothing
    }

    @Override
    public void onClick(View v) {
        if (v == stickerBtn) {
            onStickClick();
//        } else if (v == fliterBtn) {
//            onFilterClick();
        } else if (v == cropBtn) {
            onCropClick();
        } else if (v == rotateBtn) {
            onRotateClick();
        } else if (v == mTextBtn) {
            onAddTextClick();
        } else if (v == mPaintBtn) {
            onPaintClick();
        } else if (v == mBeautyBtn) {
            onBeautyClick();
        }
    }

    /**
     * 贴图模式
     *
     * @author panyi
     */
    private void onStickClick() {
        activity.bottomGallery.setCurrentItem(StickerFragment.INDEX);
        activity.mStickerFragment.onShow();
        activity.isApply = true;
    }

    /**
     * 滤镜模式
     *
     * @author panyi
     */
    private void onFilterClick() {
        activity.bottomGallery.setCurrentItem(FilterListFragment.INDEX);
        activity.mFilterListFragment.onShow();
        activity.isApply = true;
    }

    /**
     * 裁剪模式
     *
     * @author panyi
     */
    private void onCropClick() {
        activity.bottomGallery.setCurrentItem(CropFragment.INDEX);
        activity.mCropFragment.onShow();
        activity.isApply = true;
    }

    /**
     * 图片旋转模式
     *
     * @author panyi
     */
    private void onRotateClick() {
        activity.bottomGallery.setCurrentItem(RotateFragment.INDEX);
        activity.mRotateFragment.onShow();
        activity.isApply = true;
    }

    /**
     * 插入文字模式
     *
     * @author panyi
     */
    private void onAddTextClick() {
        activity.bottomGallery.setCurrentItem(AddTextFragment.INDEX);
        activity.mAddTextFragment.onShow();
        activity.isApply = true;
    }

    /**
     * 自由绘制模式
     */
    private void onPaintClick() {
        activity.bottomGallery.setCurrentItem(PaintFragment.INDEX);
        activity.mPaintFragment.onShow();
        activity.isApply = true;
    }

    private void onBeautyClick() {
        activity.bottomGallery.setCurrentItem(BeautyFragment.INDEX);
        activity.mBeautyFragment.onShow();
        activity.isApply = true;
    }

}// end class
