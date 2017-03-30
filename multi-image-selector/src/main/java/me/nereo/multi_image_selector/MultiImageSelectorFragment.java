package me.nereo.multi_image_selector;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.nereo.multi_image_selector.adapter.FolderAdapter;
import me.nereo.multi_image_selector.adapter.ImageGridAdapter;
import me.nereo.multi_image_selector.bean.Folder;
import me.nereo.multi_image_selector.bean.Image;
import me.nereo.multi_image_selector.multiimages.ImageViewTouch;
import me.nereo.multi_image_selector.multiimages.ImageViewTouchBase;
import me.nereo.multi_image_selector.multiimages.ImageViewTouchViewPager;
import me.nereo.multi_image_selector.utils.FileUtils;
import me.nereo.multi_image_selector.utils.ScreenUtils;

/**
 * Multi image selector Fragment
 * Created by Nereo on 2015/4/7.
 * Updated by nereo on 2016/5/18.
 */
public class MultiImageSelectorFragment extends Fragment {

    public static final String TAG = "MultiImageSelectorFragment";

    private static final int REQUEST_CAMERA = 100;

    private static final String KEY_TEMP_FILE = "key_temp_file";

    // Single choice
    public static final int MODE_SINGLE = 0;
    // Multi choice
    public static final int MODE_MULTI = 1;

    /**
     * Max image size，int，
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * Select mode，{@link #MODE_MULTI} by default
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    /**
     * Whether show camera，true by default
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * Original data set
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_list";

    // loaders
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    // image result data set
    private ArrayList<String> resultList = new ArrayList<>();
    // folder result data set
    private ArrayList<Folder> mResultFolder = new ArrayList<>();

    private GridView mGridView;
    private Callback mCallback;

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;

    private ListPopupWindow mFolderPopupWindow;

    private TextView mCategoryText;
    private View mPopupAnchorView;

    private boolean hasFolderGened = false;

    private File mTmpFile;
    public ImageViewTouchViewPager imageViewPager;
    private ImageAdapter adapter;
    private ImageView selectImageView;
    private View back, cancel;
    private TextView titleTextView;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (Callback) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("The Activity must implement MultiImageSelectorFragment.Callback interface...");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mis_fragment_multi_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final int mode = selectMode();
        if (mode == MODE_MULTI) {
            ArrayList<String> tmp = getArguments().getStringArrayList(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                resultList = tmp;
            }
        }
        mImageAdapter = new ImageGridAdapter(getActivity(), this, showCamera(), 3);
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);

        mPopupAnchorView = view.findViewById(R.id.category_btn);
        back = view.findViewById(R.id.back);
        cancel = view.findViewById(R.id.cancel);

        titleTextView = (TextView) view.findViewById(R.id.text_view_title);
        selectImageView = (ImageView) view.findViewById(R.id.image_view_select);
        imageViewPager = (ImageViewTouchViewPager) view.findViewById(R.id.pager);
        imageViewPager.setOnPageSelectedListener(new ImageViewTouchViewPager.OnPageSelectedListener() {
            @Override
            public void onPageSelected(int position) {
                titleTextView.setText(getString(R.string.image_number_format, position + 1, adapter.images.size()));
                if (mImageAdapter.getSelectedImages().contains(adapter.images.get(position))) {
                    selectImageView.setImageResource(R.drawable.mis_btn_selected);
                } else {
                    selectImageView.setImageResource(R.drawable.mis_btn_unselected);
                }
            }
        });
        selectImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectImageCount() == resultList.size() && !resultList.contains(adapter.images.get(imageViewPager.getCurrentItem()).path)) {
                    showTip();
                    return;
                }
                if (mImageAdapter.getSelectedImages().contains(adapter.images.get(imageViewPager.getCurrentItem()))) {
                    selectImageView.setImageResource(R.drawable.mis_btn_unselected);
                } else {
                    selectImageView.setImageResource(R.drawable.mis_btn_selected);
                }
                selectImageFromGrid(adapter.images.get(imageViewPager.getCurrentItem()), 1);

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPicture();
            }
        });
        mCategoryText = (TextView) view.findViewById(R.id.category_btn);
        mCategoryText.setText(R.string.mis_folder_all);
        mCategoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    ((MultiImageSelectorActivity) getActivity()).viewBg.setVisibility(View.VISIBLE);
                    mCategoryText.setSelected(true);
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });

        mGridView = (GridView) view.findViewById(R.id.grid);
        mGridView.setAdapter(mImageAdapter);
//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                if (mImageAdapter.isShowCamera()) {
//                    if (i == 0) {
//                        showCameraAction();
//                    } else {
//                        Image image = (Image) adapterView.getAdapter().getItem(i);
//                        selectImageFromGrid(image, mode);
//                    }
//                } else {
//                    Image image = (Image) adapterView.getAdapter().getItem(i);
//                    selectImageFromGrid(image, mode);
//                }
//            }
//        });
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == SCROLL_STATE_FLING) {
//                    Picasso.with(view.getContext()).pauseTag(TAG);
//                } else {
//                    Picasso.with(view.getContext()).resumeTag(TAG);
//                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mFolderAdapter = new FolderAdapter(getActivity());


    }

    /**
     * Create popup ListView
     */
    private void createPopupFolderList() {
        Point point = ScreenUtils.getScreenSize(getActivity());
        int width = point.x;
        int height = (int) (point.y * (4.5f / 8.0f));
        mFolderPopupWindow = new ListPopupWindow(getActivity());
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(width);
        mFolderPopupWindow.setWidth(width);
        mFolderPopupWindow.setHeight(height);
        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mFolderAdapter.setSelectIndex(i);

                final int index = i;
                final AdapterView v = adapterView;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            mCategoryText.setText(R.string.mis_folder_all);
                            if (showCamera()) {
                                mImageAdapter.setShowCamera(true);
                            } else {
                                mImageAdapter.setShowCamera(false);
                            }
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mImageAdapter.setData(folder.images);
                                mCategoryText.setText(folder.name);
                                adapter = new ImageAdapter(folder.images);
                                imageViewPager.setAdapter(adapter);
                                titleTextView.setText(getString(R.string.image_number_format, 1, folder.images.size()));
                                if (resultList != null && resultList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(resultList);
                                }
                            }
                            mImageAdapter.setShowCamera(false);
                        }

                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);

            }
        });
        mFolderPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mCategoryText.setSelected(false);
                ((MultiImageSelectorActivity) getActivity()).viewBg.setVisibility(View.GONE);
            }
        });
    }

    private void reload() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                mGridView.smoothScrollToPosition(0);
            }
        }, 1000);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_TEMP_FILE, mTmpFile);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mTmpFile = (File) savedInstanceState.getSerializable(KEY_TEMP_FILE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // load image data
        getActivity().getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    if (mCallback != null) {
                        mCallback.onCameraShot(mTmpFile);
                    }
                }
            } else {
                // delete tmp file
                while (mTmpFile != null && mTmpFile.exists()) {
                    boolean success = mTmpFile.delete();
                    if (success) {
                        mTmpFile = null;
                    }
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * Open camera
     */
    public void showCameraAction() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    1);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                try {
                    mTmpFile = FileUtils.createTmpFile(getActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mTmpFile != null && mTmpFile.exists()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else {
                    Toast.makeText(getActivity(), R.string.mis_error_image_not_exist, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), R.string.mis_msg_no_camera, Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void requestPermission(final String permission, String rationale, final int requestCode) {
//        if (shouldShowRequestPermissionRationale(permission)) {
//            new AlertDialog.Builder(getContext())
//                    .setTitle(R.string.mis_permission_dialog_title)
//                    .setMessage(rationale)
//                    .setPositiveButton(R.string.mis_permission_dialog_ok, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            requestPermissions(new String[]{permission}, requestCode);
//                        }
//                    })
//                    .setNegativeButton(R.string.mis_permission_dialog_cancel, null)
//                    .create().show();
//        } else {
//            requestPermissions(new String[]{permission}, requestCode);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCameraAction();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.permission);
                builder.setMessage(R.string.permission_sd_hint);
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton(R.string.go_set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.parse("package:com.kuiyuan.aogou")));
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * notify callback
     *
     * @param image image data
     */
    public void selectImageFromGrid(Image image, int mode) {
        if (image != null) {
            if (mode == MODE_MULTI) {
                if (resultList.contains(image.path)) {
                    resultList.remove(image.path);
                    if (mCallback != null) {
                        mCallback.onImageUnselected(image.path);
                    }
                } else {
                    if (selectImageCount() == resultList.size()) {
                        showTip();
                        return;
                    }
                    resultList.add(image.path);
                    if (mCallback != null) {
                        mCallback.onImageSelected(image.path);
                    }
                }
                mImageAdapter.select(image);
            } else if (mode == MODE_SINGLE) {
                if (mCallback != null) {
                    mCallback.onSingleImageSelected(image.path);
                }
            }
        }
    }

    public void selectImageFromGrid(String image) {
        if (image.equals(adapter.images.get(imageViewPager.getCurrentItem()).path)) {
            selectImageView.setImageResource(R.drawable.mis_btn_unselected);
        }
        if (image != null) {
            if (resultList.contains(image)) {
                resultList.remove(image);
                if (mCallback != null) {
                    mCallback.onImageUnselected(image);
                }
            } else {
                if (selectImageCount() == resultList.size()) {
                    showTip();
                    return;
                }
                resultList.add(image);
                if (mCallback != null) {
                    mCallback.onImageSelected(image);
                }
            }
            mImageAdapter.select(image);

        }
    }

    public void selectImage(String image) {
        if (image.equals(adapter.images.get(imageViewPager.getCurrentItem()).path)) {
            selectImageView.setImageResource(R.drawable.mis_btn_unselected);
        }
        if (image != null) {
            if (resultList.contains(image)) {
                resultList.remove(image);
                if (mCallback != null) {
                    mCallback.onImageUnselected(image);
                }
            } else {
                if (selectImageCount() == resultList.size()) {
                    showTip();
                    return;
                }
                resultList.add(image);
                if (mCallback != null) {
                    mCallback.onImageSelected(image);
                }
                reload();
            }
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = null;
            if (id == LOADER_ALL) {
                cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                        new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
            } else if (id == LOADER_CATEGORY) {
                cursorLoader = new CursorLoader(getActivity(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'",
                        null, IMAGE_PROJECTION[2] + " DESC");
            }
            return cursorLoader;
        }

        private boolean fileExist(String path) {
            if (!TextUtils.isEmpty(path)) {
                return new File(path).exists();
            }
            return false;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                if (data.getCount() > 0) {
                    ArrayList<Image> images = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        if (!fileExist(path)) {
                            continue;
                        }
                        Image image = null;
                        if (!TextUtils.isEmpty(name)) {
                            image = new Image(path, name, dateTime);
                            images.add(image);
                        }
                        if (!hasFolderGened) {
                            // get all folder data
                            File folderFile = new File(path).getParentFile();
                            if (folderFile != null && folderFile.exists()) {
                                String fp = folderFile.getAbsolutePath();
                                Folder f = getFolderByPath(fp);
                                if (f == null) {
                                    Folder folder = new Folder();
                                    folder.name = folderFile.getName();
                                    folder.path = fp;
                                    folder.cover = image;
                                    ArrayList<Image> imageList = new ArrayList<>();
                                    imageList.add(image);
                                    folder.images = imageList;
                                    mResultFolder.add(folder);
                                } else {
                                    f.images.add(image);
                                }
                            }
                        }

                    } while (data.moveToNext());

                    mImageAdapter.setData(images);
                    adapter = new ImageAdapter(images);
                    imageViewPager.setAdapter(adapter);
                    titleTextView.setText(getString(R.string.image_number_format, 1, images.size()));

                    if (resultList != null && resultList.size() > 0) {
                        mImageAdapter.setDefaultSelected(resultList);
                    }
                    if (!hasFolderGened) {
                        mFolderAdapter.setData(mResultFolder);
                        hasFolderGened = true;
                    }
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    public void showPicture(Image image) {
        back.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        titleTextView.setVisibility(View.VISIBLE);
        mCategoryText.setVisibility(View.GONE);
        selectImageView.setVisibility(View.VISIBLE);
        imageViewPager.setVisibility(View.VISIBLE);
        imageViewPager.setCurrentItem(adapter.images.indexOf(image));
        if (mImageAdapter.getSelectedImages().contains(image)) {
            selectImageView.setImageResource(R.drawable.mis_btn_selected);
        } else {
            selectImageView.setImageResource(R.drawable.mis_btn_unselected);
        }
    }

    public void dismissPicture() {
        back.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.GONE);
        mCategoryText.setVisibility(View.VISIBLE);
        selectImageView.setVisibility(View.GONE);
        imageViewPager.setVisibility(View.GONE);
    }

    private Folder getFolderByPath(String path) {
        if (mResultFolder != null) {
            for (Folder folder : mResultFolder) {
                if (TextUtils.equals(folder.path, path)) {
                    return folder;
                }
            }
        }
        return null;
    }

    private boolean showCamera() {
        return getArguments() == null || getArguments().getBoolean(EXTRA_SHOW_CAMERA, true);
    }

    private int selectMode() {
        return getArguments() == null ? MODE_MULTI : getArguments().getInt(EXTRA_SELECT_MODE);
    }

    private int selectImageCount() {
        return getArguments() == null ? 9 : getArguments().getInt(EXTRA_SELECT_COUNT);
    }

    /**
     * Callback for host activity
     */
    public interface Callback {
        void onSingleImageSelected(String path);

        void onImageSelected(String path);

        void onImageUnselected(String path);

        void onCameraShot(File imageFile);
    }


    class ImageAdapter extends PagerAdapter {
        private ArrayList<Image> images;

        public ImageAdapter(ArrayList<Image> images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageViewTouch photoView = new ImageViewTouch(container.getContext(), null);
            String imageUrl = images.get(position).path;
//            Picasso.with(getActivity()).load(new File(imageUrl)).into(photoView);
            Glide.with(getActivity()).load(imageUrl).into(photoView);

            photoView.setTag(ImageViewTouchViewPager.VIEW_PAGER_OBJECT_TAG + position);
            photoView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
            photoView.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {

                @Override
                public void onSingleTapConfirmed() {
//                    finish();
                }
            });
            container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);
            return photoView;
        }
    }

    private void showTip() {
        View activeView = LayoutInflater.from(getActivity()).inflate(R.layout.image_view_dialog_tip, null);
        final Dialog activeDialog = new Dialog(getActivity(), R.style.ImageAlertDialog);
        activeDialog.setContentView(activeView);
        ((TextView) activeView.findViewById(R.id.titleTextView)).setText(getString(R.string.most_hint, selectImageCount()));
        activeView.findViewById(R.id.positiveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activeDialog.dismiss();
            }
        });
        activeDialog.show();
    }
}
