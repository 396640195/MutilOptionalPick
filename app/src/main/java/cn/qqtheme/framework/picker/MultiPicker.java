package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.qqtheme.framework.widget.WheelView;
import cn.qqtheme.framework.widget.WheelView.OnItemSelectListener;

public class MultiPicker extends WheelPicker {
	private int textSize = WheelView.TEXT_SIZE;
	private WheelView wheelView1;
	private WheelView wheelView2;
	private Map<String,List<String>> mMapItems;
	private List<String> mItemsLeft;
	private OnMultiPickListener listener;
	
	public MultiPicker(Activity activity) {
		super(activity);
	}

	public MultiPicker setItems(Map<String,List<String>> items) {
		if(items != null && items.size() > 0){
			mMapItems = items;
			mItemsLeft = new ArrayList<>();
			Set<String> set = items.keySet();
			if(set.size() > 0) {
				for(String item: set){
					mItemsLeft.add(item);
				}
			}
			if(wheelView1 != null) {
				wheelView1.setItems(mItemsLeft);
				wheelView2.setItems(mMapItems.get(mItemsLeft.get(0)));
			}
		}
		return this;
	}

	public MultiPicker setTitleBackgroundColor(@ColorInt int color){
		this.setTopBackgroundColor(color);
		return this;
	}

	@Override
	protected View makeCenterView() {

		LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);
        wheelView1 = createWheelView();
        wheelView2 = createWheelView();
        wheelView1.setUseWeight(true);
        wheelView2.setUseWeight(true);
        wheelView1.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        wheelView2.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, 1.0f));
        wheelView1.setOnItemSelectListener(new OnItemSelectListener() {
			@Override
			public void onSelected(int index) {
				List<String> items = mMapItems.get(mItemsLeft.get(index));
				wheelView2.setItems( items, items.size() /2 );
			}
		});
		wheelView1.setItems(mItemsLeft,mItemsLeft.size()/2);
		List<String> items = mMapItems.get(mItemsLeft.get(0));
		wheelView2.setItems(items,items.size()/2);
        wheelView1.setTextSize(textSize);
        wheelView2.setTextSize(textSize);
        layout.addView(wheelView1);
        layout.addView(wheelView2);
		return layout;
	}
	

	
	@Override
	protected void onSubmit() {
		super.onSubmit();
		if (listener != null) {
			String item = mItemsLeft.get(wheelView1.getSelectedIndex());
			listener.onMultiPick(item, mMapItems.get(item).get(wheelView2.getSelectedIndex()));
		}
	}
	
	public MultiPicker setOnMultiPickListener(OnMultiPickListener listener) {
		this.listener = listener;
		return this;
	}

	public interface OnMultiPickListener {
		void onMultiPick(String item1, String item2);
	}
}
