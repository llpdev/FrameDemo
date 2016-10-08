package llpandroid.com.popuwindow.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;

import llpandroid.com.popuwindow.R;
import llpandroid.com.popuwindow.adapter.TextAdapter;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {
	
	private ListView regionListView;
	private ListView plateListView;
	private ArrayList<String> groups = new ArrayList<String>();
	private LinkedList<String> childrenItem = new LinkedList<String>();
	private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
	private TextAdapter plateListViewAdapter;
	private TextAdapter earaListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;
	private String showString = "不限";

	private String[] names = {"不限", "宝安", "龙岗", "龙华", "南山", "福田", "罗湖", "坪山", "光明", "盐田", "大鹏", "其他"};
	private String[][] itemName = {{}, {"不限", "碧海湾", "宝安中心区", "福永", "石岩", "沙井", "松岗", "新安", "西乡"}
			, {"不限", "横岗", "龙岗中心区", "坪地", "布吉", "平湖", "坂田", "大运"}
			, {"不限", "龙华", "观澜", "红山"}, {"不限", "后海", "蛇口", "前海", "深圳湾", "华侨城", "大南山", "科技园", "南油", "南头", "南山中心区", "沙河", "西丽"}, {"不限", "保税区", "八卦岭", "车公庙上下沙", "福田中心区", "华强", "皇岗", "景田", "莲花", "梅林", "石厦", "上步", "新洲", "香蜜湖", "竹子林"}
			, {"不限", "布心草铺", "翠竹", "蔡屋围", "东门", "洪湖", "黄贝岭", "莲塘", "泥岗", "人民南", "笋岗", "水库", "文锦渡口岸", "银湖"}
			, {"不限", "坑悻", "坪山"}
			, {"不限", "光明", "公明"}
			, {"不限", "大小梅沙", "沙头角", "盐田港"}
			, {"不限", "葵涌", "南澳", "大鹏"}
			, {"不限", "别墅购房团", "东莞", "贵州花果园", "惠州", "汕尾", "威海", "中山"}};


	public ViewMiddle(Context context) {
		super(context);
		init(context);
	}

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).equals(showArea)) {
				earaListViewAdapter.setSelectedPosition(i);
				childrenItem.clear();
				if (i < children.size()) {
					childrenItem.addAll(children.get(i));
				}
				tEaraPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
				plateListViewAdapter.setSelectedPosition(j);
				tBlockPosition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
//		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_left));
		setBackgroundResource(R.drawable.choosearea_bg_mid);

		//初始化下拉列表展示的数据
		for (int i = 0; i < names.length; i++) {
			groups.add(names[i]);
			LinkedList<String> tItem = new LinkedList<String>();
			for (int j = 0; j < itemName[i].length; j++) {
				tItem.add(itemName[i][j]);
			}
			children.put(i, tItem);
		}

		/*for(int i=0;i<10;i++){
			groups.add(i+"行");
			LinkedList<String> tItem = new LinkedList<String>();
			for(int j=0;j<15;j++){
				
				tItem.add(i+"行"+j+"列");
				
			}
			children.put(i, tItem);
		}*/

		earaListViewAdapter = new TextAdapter(context, groups,
				R.drawable.choose_item_selected,
				R.drawable.choose_eara_item_selector);
		earaListViewAdapter.setTextSize(17);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		earaListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, int position) {
						//设置第二个可见
						plateListView.setVisibility(VISIBLE);
						if (position < children.size()) {
							childrenItem.clear();
							childrenItem.addAll(children.get(position));
							plateListViewAdapter.notifyDataSetChanged();
						}
					}
				});
		if (tEaraPosition < children.size())
			childrenItem.addAll(children.get(tEaraPosition));
		plateListViewAdapter = new TextAdapter(context, childrenItem,
				R.drawable.choose_item_right,
				R.drawable.choose_plate_item_selector);
		plateListViewAdapter.setTextSize(15);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListViewAdapter
				.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(View view, final int position) {
						
						showString = childrenItem.get(position);
						if (mOnSelectListener != null) {
							
							mOnSelectListener.getValue(showString);
						}

					}
				});
		if (tBlockPosition < childrenItem.size())
			showString = childrenItem.get(tBlockPosition);
		if (showString.contains("不限")) {
			showString = showString.replace("不限", "");
		}
		setDefaultSelect();

	}

	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}

	public String getShowText() {
		return showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText);
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}
}
