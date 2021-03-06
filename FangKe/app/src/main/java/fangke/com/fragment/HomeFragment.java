package fangke.com.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fangke.com.activity.BrainBuyActivity;
import fangke.com.activity.DiscountActivity;
import fangke.com.activity.FindHouseActivity;
import fangke.com.activity.HomeActivity;
import fangke.com.activity.HousePriceActivity;
import fangke.com.activity.LocationMapActivity;
import fangke.com.activity.MapActivity;
import fangke.com.activity.NewHouseActivity;
import fangke.com.activity.OverseasHouseActivity;
import fangke.com.activity.R;
import fangke.com.activity.ReducePriceActivity;
import fangke.com.activity.RentalHouseActivity;
import fangke.com.activity.SecondHandHouseActivity;
import fangke.com.activity.SellHouseActivity;
import fangke.com.activity.ShopOfficeActivity;
import fangke.com.view.HomeLinearLayout;
import utils.DispalyUtil;

import static android.content.ContentValues.TAG;

/**
 * 主页
 *
 * @author ChongZi007
 * @time 2017/3/31 10:09
 * @参数
 * @return
 */
public class HomeFragment extends Fragment {
    private ListView ls;
    private GridView gv;
    private String[] title;
    private int[] imags;
    private View view;
    private HomeActivity mActivity;
    private RelativeLayout rl_top;
    private ImageView imageView;
    private HomeLinearLayout ll;
    private int mLastY = -63;//记录上一次headview滑动的坐标
    private View headView;
    private int[] location;
    private ImageView img_searchFramwork;
    private ImageView img_saosao;
    private ImageView img_sousuo;
    private TextView tv_headTitleforsearch;
    private RelativeLayout home_top_top;
    private RelativeLayout.LayoutParams home_top_topLayoutParams;
    private VelocityTracker mVelocityTracker;
    private ImageView img_jiangjia;
    private ImageView img_clever;
    private ImageView img_youhui;
    private TextView tv_area;
    private ImageView img_area;
    private ImageView img_map;
    private TextView tv_map;
    private SharedPreferences sp;
    final String[] PERMISSION = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,  //定位权限
    };


    public HomeFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        mActivity = (HomeActivity) getActivity();
        initViews();
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        sp = mActivity.getSharedPreferences("maps", Context.MODE_PRIVATE);
        String location = sp.getString("location", "---");
        tv_area.setText(location);
        super.onResume();
    }

    /**
     * 初始化布局
     */
    private void initViews() {
        //listview的头view
        headView = View.inflate(mActivity, R.layout.home_listview_headview, null);
        title = new String[]{"新房", "二手房", "租房", "商铺写字楼", "卖房", "海外房产", "帮你找房", "小区房价"};
        imags = new int[]{
                R.drawable.home_gv_1, R.drawable.home_gv_2, R.drawable.home_gv_3,
                R.drawable.home_gv_4, R.drawable.home_gv_5, R.drawable.home_gv_6,
                R.drawable.home_gv_7, R.drawable.home_gv_8,
        };
        home_top_top = (RelativeLayout) view.findViewById(R.id.home_top_top_relativelayout);//包住最上层固定布局的布局
        ls = (ListView) view.findViewById(R.id.ls_home);//listview
        gv = (GridView) headView.findViewById(R.id.gv_home);//GridView
        imageView = (ImageView) view.findViewById(R.id.imageView);
        //定位文字
        tv_area = (TextView) view.findViewById(R.id.home_tv_area);
        //定位白色小三角
        SharedPreferences sp = mActivity.getSharedPreferences("maps", Context.MODE_PRIVATE);
        String location = sp.getString("location", "---");
        tv_area.setText(location);
        img_area = (ImageView) view.findViewById(R.id.home_img_area);
        //map
        img_map = (ImageView) view.findViewById(R.id.home_img_map);
        tv_map = (TextView) view.findViewById(R.id.home_tv_map);
        img_searchFramwork = (ImageView) view.findViewById(R.id.home_serch_framwork);//搜索白框
        img_saosao = (ImageView) view.findViewById(R.id.home_img_sao_sao);//扫一扫图片
        ll = (HomeLinearLayout) view.findViewById(R.id.home_ll);//要滑动的线性布局
        img_sousuo = (ImageView) view.findViewById(R.id.home_img_sousuo);//放大镜的那个图片
        img_youhui = (ImageView) headView.findViewById(R.id.home_img_youhui);  //独家优惠
        img_jiangjia = (ImageView) headView.findViewById(R.id.home_img_jiangjia);//降价楼盘
        img_clever = (ImageView) headView.findViewById(R.id.home_img_clever); //智能买房
        tv_headTitleforsearch = (TextView) view.findViewById(R.id.home_tv_headTitleforsearch);//放大镜隔壁的图片
        rl_top = (RelativeLayout) view.findViewById(R.id.home_top_relativelayout);//固定最上层的相对布局
        //获取顶部绝对布局的参数
        home_top_topLayoutParams = (RelativeLayout.LayoutParams) home_top_top.getLayoutParams();
        ls.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, getData()));
        ls.addHeaderView(headView);
        ls.setSelectionFromTop(0, -140);
        gv.setAdapter(new HomeFragment.mGridViewAdapter());
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
        this.location = new int[2];
    }


    /**
     * 初始化侦听
     */
    private void initListener() {
        //注册listview的滑动侦听事件 通过得到头view的坐标的改变通过临界点的判断达到背景颜色渐变以及搜索框伸缩的功能
        ls.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_FLING:
                        int[] lo = new int[2];
                        headView.getLocationOnScreen(lo);

                        if (lo[1] < -73) {
                            Drawable rl_topBackground = rl_top.getBackground();
                            rl_topBackground.setAlpha(255);
                            home_top_topLayoutParams.setMargins(0, 0, 0, 0);
                            home_top_top.setLayoutParams(home_top_topLayoutParams);
                        }
                        if (lo[1] >= 60) {
                            Drawable rl_topBackground = rl_top.getBackground();
                            rl_topBackground.setAlpha(0);
                            home_top_topLayoutParams.setMargins(-(DispalyUtil.dip2px(mActivity, 50)), DispalyUtil.dip2px(mActivity, 50),
                                    -(DispalyUtil.dip2px(mActivity, 50)), 0);
                            home_top_top.setLayoutParams(home_top_topLayoutParams);

                        }


                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //headview已经获取 这里我们获取他的坐标即可
                //得到我们的白框
                headView.getLocationOnScreen(location);
                Drawable rl_topBackground = rl_top.getBackground();
                int offY = location[1] - mLastY;
                if (location[1] >= -63 && location[1] <= 66) {
                    //相对布局的top从40到0  left -50到0 right -60到0
                    //改变rl的透明度
                    if (location[1] >= -63 && location[1] <= -60) {
                        rl_topBackground.setAlpha(255);
                    } else if (location[1] >= -47 && location[1] <= -43) {
                        rl_topBackground.setAlpha(230);
                    } else if (location[1] >= -42 && location[1] <= -33) {
                        rl_topBackground.setAlpha(215);
                    } else if (location[1] >= -32 && location[1] <= -43) {
                        rl_topBackground.setAlpha(190);
                    } else if (location[1] >= -22 && location[1] <= -43) {
                        rl_topBackground.setAlpha(165);
                    } else if (location[1] >= -12 && location[1] <= -43) {
                        rl_topBackground.setAlpha(140);
                    } else if (location[1] >= -2 && location[1] <= -43) {
                        rl_topBackground.setAlpha(125);
                    } else if (location[1] >= 2 && location[1] <= 11) {
                        rl_topBackground.setAlpha(100);
                    } else if (location[1] >= 12 && location[1] <= 21) {
                        rl_topBackground.setAlpha(80);
                    } else if (location[1] >= 22 && location[1] <= -31) {
                        rl_topBackground.setAlpha(60);
                    } else if (location[1] >= 32 && location[1] <= 41) {
                        rl_topBackground.setAlpha(40);
                    } else if (location[1] >= 42 && location[1] <= 51) {
                        rl_topBackground.setAlpha(20);
                    } else if (location[1] >= 52 && location[1] <= 63) {
                        rl_topBackground.setAlpha(0);
                    }

                }
       if(headView.getWidth()<800){

           if (offY > 0 && location[1] > -66 && location[1] <= 47) {
               //下滑
               home_top_topLayoutParams.setMargins(home_top_topLayoutParams.leftMargin - (int) (offY * 1.0),
                       home_top_topLayoutParams.topMargin + offY,
                       home_top_topLayoutParams.rightMargin - (int) (offY * 1.0), 0);
               home_top_top.setLayoutParams(home_top_topLayoutParams);
           } else if (offY < 0 && location[1] > -66 && location[1] <= 47) {
               //上滑
               home_top_topLayoutParams.setMargins(home_top_topLayoutParams.leftMargin - offY,
                       home_top_topLayoutParams.topMargin + offY,
                       home_top_topLayoutParams.rightMargin - offY, 0);
               home_top_top.setLayoutParams(home_top_topLayoutParams);
           } else if (location[1] <= -66) {

               rl_topBackground.setAlpha(255);
               home_top_topLayoutParams.setMargins(0, 0, 0, 0);
               home_top_top.setLayoutParams(home_top_topLayoutParams);
           } else if (location[1] > 46) {
               rl_topBackground.setAlpha(0);
               home_top_topLayoutParams.setMargins(-(DispalyUtil.dip2px(mActivity, 56)),
                       DispalyUtil.dip2px(mActivity, 56),
                       -(DispalyUtil.dip2px(mActivity, 56)), 0);
               home_top_top.setLayoutParams(home_top_topLayoutParams);
           }


       }else{
           if (offY > 0 && location[1] > -66 && location[1] <= 71) {
               //下滑
               home_top_topLayoutParams.setMargins(home_top_topLayoutParams.leftMargin - (int) (offY * 1.0),
                       home_top_topLayoutParams.topMargin + offY,
                       home_top_topLayoutParams.rightMargin - (int) (offY * 1.0), 0);
               home_top_top.setLayoutParams(home_top_topLayoutParams);
           } else if (offY < 0 && location[1] > -66 && location[1] <= 71) {
               //上滑
               home_top_topLayoutParams.setMargins(home_top_topLayoutParams.leftMargin - offY,
                       home_top_topLayoutParams.topMargin + offY,
                       home_top_topLayoutParams.rightMargin - offY, 0);
               home_top_top.setLayoutParams(home_top_topLayoutParams);
           } else if (location[1] <= -66) {

               rl_topBackground.setAlpha(255);
               home_top_topLayoutParams.setMargins(0, 0, 0, 0);
               home_top_top.setLayoutParams(home_top_topLayoutParams);
           } else if (location[1] > 71) {
               rl_topBackground.setAlpha(0);
               home_top_topLayoutParams.setMargins(-(DispalyUtil.dip2px(mActivity, 45)),
                       DispalyUtil.dip2px(mActivity, 45),
                       -(DispalyUtil.dip2px(mActivity, 45)), 0);
               home_top_top.setLayoutParams(home_top_topLayoutParams);
           }


       }
                mLastY = location[1];
            }
        });

        // 处理 gridview的点击事件
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(mActivity, NewHouseActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(mActivity, SecondHandHouseActivity.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(mActivity, RentalHouseActivity.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(mActivity, ShopOfficeActivity.class);
                        startActivity(intent3);
                        break;
                    case 4:
                        Intent intent4 = new Intent(mActivity, SellHouseActivity.class);
                        startActivity(intent4);
                        break;
                    case 5:
                        Intent intent5 = new Intent(mActivity, OverseasHouseActivity.class);
                        startActivity(intent5);
                        break;
                    case 6:
                        Intent intent6 = new Intent(mActivity, FindHouseActivity.class);
                        startActivity(intent6);
                        break;
                    case 7:
                        Intent intent7 = new Intent(mActivity, HousePriceActivity.class);
                        startActivity(intent7);
                        break;
                    default:
                        break;
                }
            }
        });

        //处理降价楼盘 独家优惠 还有智能买房的点击事件
        img_youhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //独家优惠
                Intent intent = new Intent(mActivity, DiscountActivity.class);
                startActivity(intent);
            }
        });
        img_jiangjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //降价楼盘
                Intent intent = new Intent(mActivity, ReducePriceActivity.class);
                startActivity(intent);
            }
        });
        img_clever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //智能买房
                Intent intent = new Intent(mActivity, BrainBuyActivity.class);
                startActivity(intent);
            }
        });

        tv_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定位
                Intent intent = new Intent(mActivity, LocationMapActivity.class);
                if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //Android 6.0申请权限
                    ActivityCompat.requestPermissions(mActivity, PERMISSION, 1);

                } else {
                    startActivity(intent);
                }




            }
        });


        img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //定位小按钮
                Intent intent = new Intent(mActivity, MapActivity.class);
                startActivity(intent);
            }
        });


        tv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //map
                Intent intent = new Intent(mActivity, MapActivity.class);
                startActivity(intent);


            }
        });


        img_area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //map小按钮
                Intent intent = new Intent(mActivity, LocationMapActivity.class);
                startActivity(intent);
            }
        });

        img_saosao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //二维码小按钮
                Toast.makeText(mActivity, "哈哈哈哈哈哈哈哈，我是二维码扫一扫你好", Toast.LENGTH_SHORT).show();
            }
        });

        img_searchFramwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索白框小按钮
                Toast.makeText(mActivity, "哈哈哈哈哈哈哈哈，你要搜索了吗", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private List<String> getData() {

        List<String> data = new ArrayList<String>();
        data.add("测试数据1");
        data.add("测试数据2");
        data.add("测试数据3");
        data.add("测试数据4");
        data.add("测试数据5");
        data.add("测试数据6");
        data.add("测试数据7");
        data.add("测试数据8");
        data.add("测试数据9");
        data.add("测试数据10");
        data.add("测试数据11");
        data.add("测试数据12");
        data.add("测试数据13");
        data.add("测试数据14");
        data.add("测试数据15");
        data.add("测试数据16");
        data.add("测试数据17");
        data.add("测试数据18");
        return data;
    }

    /**
     * 实现gridview用的适配器
     */
    class mGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imags.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.home_grid_item, null);
                viewHolder = new HomeFragment.GridViewHolder();
                viewHolder.tlt = (TextView) convertView.findViewById(R.id.home_grid_tv);
                viewHolder.image = (ImageView) convertView.findViewById(R.id.home_grid_img);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (HomeFragment.GridViewHolder) convertView.getTag();
            }
            viewHolder.tlt.setText(title[position]);
            viewHolder.image.setImageResource(imags[position]);

            ViewGroup.LayoutParams params = viewHolder.image.getLayoutParams();
            params.width = DispalyUtil.dip2px(mActivity, 45);
            params.height = DispalyUtil.dip2px(mActivity, 45);
            viewHolder.image.setLayoutParams(params);


            return convertView;
        }
    }

    class GridViewHolder {
        public TextView tlt;
        public ImageView image;
    }


}
