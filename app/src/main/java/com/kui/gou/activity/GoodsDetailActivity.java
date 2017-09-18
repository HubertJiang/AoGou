package com.kui.gou.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.TextView;

import com.kui.gou.R;
import com.kui.gou.adapter.ImageAdapter;
import com.kui.gou.entity.Goods;
import com.kui.gou.entity.User;
import com.kui.gou.view.CirclePageIndicator;

public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    private String id;
    private TextView content, likesTextView, serviceTextView;
    private ViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Goods goods;
    private ImageAdapter adapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        id = getIntent().getStringExtra("id");

        content = (TextView) findViewById(R.id.content);
        likesTextView = (TextView) findViewById(R.id.likes_text_view);
        serviceTextView = (TextView) findViewById(R.id.service_text_view);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);

        adapter = new ImageAdapter();
        likesTextView.setOnClickListener(this);
        serviceTextView.setOnClickListener(this);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setEnabled(false);

//        BmobQuery<Goods> query = new BmobQuery<>();
//        query.getObject(id, new QueryListener<Goods>() {
//
//            @Override
//            public void done(Goods object, BmobException e) {
//                goods = object;
//                swipeRefreshLayout.setRefreshing(false);
//                if (e == null) {
//                    getSupportActionBar().setTitle(object.name);
//                    content.setText(object.content);
//                    ArrayList<BmobFile> images = new ArrayList<>();
//                    images.add(object.image);
//                    if (object.image1 != null) {
//                        images.add(object.image1);
//                    }
//                    if (object.image2 != null) {
//                        images.add(object.image2);
//                    }
//                    if (object.image3 != null) {
//                        images.add(object.image3);
//                    }
//                    adapter.setImages(images);
//                    viewPager.setAdapter(adapter);
//                    circlePageIndicator.setViewPager(viewPager);
//
//                } else {
//                    AoApplication.showToast(R.string.no_network);
//                }
//            }
//
//        });

    }

//    private void get() {
//        BmobQuery<Goods> query = new BmobQuery<>();
//        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
//        List<BmobQuery<Goods>> queries = new ArrayList<>();
//        queries.add(new BmobQuery<Goods>().addWhereEqualTo("objectId", id));
//        queries.add(new BmobQuery<Goods>().addWhereRelatedTo("likes", new BmobPointer(user)));
//        query.and(queries);
//        query.findObjects(new FindListener<Goods>() {
//            @Override
//            public void done(List<Goods> list, BmobException e) {
//                if (e == null) {
//                    if (list.size() > 0) {
//                        likesTextView.setText(R.string.already_favorite);
//                    } else {
//                        likesTextView.setText(R.string.favorite);
//                    }
//                } else {
//                    AoApplication.showToast(R.string.no_network);
//                }
//            }
//
//        });
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.likes_text_view:
//                if (user == null) {
//                    startActivity(new Intent(this, SignInActivity.class));
//                } else {
//                    likesTextView.setEnabled(false);
//                    swipeRefreshLayout.setRefreshing(true);
//                    BmobRelation relation = new BmobRelation();
//                    if (likesTextView.getText().equals(getString(R.string.favorite))) {
//                        relation.add(goods);
//                    } else {
//                        relation.remove(goods);
//                    }
//                    User newUser = new User();
//                    newUser.setLikes(relation);
//                    newUser.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
//                        @Override
//                        public void done(BmobException e) {
//                            swipeRefreshLayout.setRefreshing(false);
//                            likesTextView.setEnabled(true);
//                            if (e == null) {
//                                if (likesTextView.getText().equals(getString(R.string.favorite))) {
//                                    likesTextView.setText(R.string.already_favorite);
//                                } else {
//                                    likesTextView.setText(R.string.favorite);
//                                }
//                            } else {
//                                AoApplication.showToast(R.string.no_network);
//                            }
//                        }
//                    });
//                }
//                break;
//            case R.id.service_text_view:
//                if (user == null) {
//                    startActivity(new Intent(this, SignInActivity.class));
//                } else {
//                    Information info = new Information();
//                    info.setAppkey(Constant.SERVICE_KEY);
//                    info.setColor("#3F51B5");
//                    info.setUname(BmobUser.getCurrentUser(User.class).getNickname());
//                    info.setPhone(BmobUser.getCurrentUser(User.class).getMobilePhoneNumber());
//                    info.setFace(BmobUser.getCurrentUser(User.class).getAvatar() == null ? null : BmobUser.getCurrentUser(User.class).getAvatar().getUrl());
//                    //咨询内容
//                    ConsultingContent consultingContent = new ConsultingContent();
//                    //咨询内容标题，必填
//                    consultingContent.setSobotGoodsTitle(goods.name);
//                    //咨询内容图片，选填 但必须是图片地址
//                    consultingContent.setSobotGoodsImgUrl(goods.image.getFileUrl());
//                    //描述，选填
//                    consultingContent.setSobotGoodsDescribe(goods.content);
//                    //标签，选填
//                    consultingContent.setSobotGoodsLable(goods.price + "");
//                    consultingContent.setSobotGoodsFromUrl(goods.getObjectId());
//                    info.setConsultingContent(consultingContent);
//                    SobotApi.startSobotChat(this, info);
//                    break;
//                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        user = BmobUser.getCurrentUser(User.class);
//        if (user != null) {
//            get();
//        }
    }
}
