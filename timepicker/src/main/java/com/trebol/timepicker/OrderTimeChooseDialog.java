package com.trebol.timepicker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 作者：gaohc on 2016/10/26 13:45
 * ©2016 中国高端品牌网 All Rights Reserved.
 */
public class OrderTimeChooseDialog extends BottomSheetDialog {

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.date_picker)
    private NumberPicker mDatePicker;

    @ViewInject(R.id.hour_picker)
    private NumberPicker mHourPicker;

    @ViewInject(R.id.minute_picker)
    private NumberPicker mMinutePicker;

    public OrderTimeChooseDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.bottom_dialog);
        x.view().inject(this, findViewById(R.id.bottom_dialog));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mDatePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mHourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mMinutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        mDatePicker.setMinValue(0);
        mDatePicker.setMaxValue(3);
        mDatePicker.setDisplayedValues(new String[]{"10.27 10:30-12:30",
                "10.27 16:30-20:30",
                "10.28 10:30-12:30",
                "10.28 16:30-20:30"});
        mDatePicker.setWrapSelectorWheel(false);

        mDatePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (oldVal == newVal) {
                    return;
                }
                updateHourPicker();
            }
        });

        mHourPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (oldVal == newVal) {
                    return;
                }
                updateMinutePicker();
            }
        });

        updateHourPicker();
        updateMinutePicker();
    }

    @Event(R.id.action_ok)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.action_ok:
                String time = String.format("%s %s:%s",
                        mDatePicker.getDisplayedValues()[mDatePicker.getValue()].substring(0, 5),
                        mHourPicker.getDisplayedValues()[mHourPicker.getValue()],
                        mMinutePicker.getDisplayedValues()[mMinutePicker.getValue()]);
                Toast.makeText(getContext(), time, Toast.LENGTH_SHORT).show();
                dismiss();

        }
    }

    private void updateHourPicker() {
        int position = mDatePicker.getValue();
        int start, end;
        if (position == 0 || position == 2) {
            start = 10;
            end = 12;
        } else {
            start = 16;
            end = 20;
        }
        updatePicker(mHourPicker, start, end, 24);
        updateMinutePicker();
    }

    private void updateMinutePicker() {
        int hour = Integer.parseInt(mHourPicker.getDisplayedValues()[mHourPicker.getValue()]);

        int start, end;
        if (hour == 10 || hour == 16) {
            start = 30;
            end = 59;
        } else if (hour == 12 || hour == 20) {
            start = 0;
            end = 30;
        } else {
            start = 0;
            end = 59;
        }
        updatePicker(mMinutePicker, start, end, 60);
    }

    private void updatePicker(NumberPicker picker, int start, int end, int max) {
        String[] values = new String[end - start + 1];
        for (int i = 0; i < values.length; i++) {
            values[i] = String.format("%02d", start + i);
        }
        picker.setMaxValue(0);
        picker.setDisplayedValues(values);
        picker.setMinValue(0);
        picker.setMaxValue(values.length - 1);
        if (values.length == max) {
            picker.setWrapSelectorWheel(true);
        } else if (values.length > 2) {
            picker.setWrapSelectorWheel(false);
            picker.setValue(2);
        } else {
            mMinutePicker.setWrapSelectorWheel(false);
        }
        picker.setValue(0);
    }

//    private class TestAdapter extends RecyclerView.Adapter<TestViewHolder> {
//
//        @Override
//        public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bottom_dialog_item, null);
//            return new TestViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(TestViewHolder holder, int position) {
//            holder.bindData("测试条目" + (position + 1));
//        }
//
//        @Override
//        public int getItemCount() {
//            return 10;
//        }
//    }


//    private class TestViewHolder extends RecyclerView.ViewHolder {
//
//        @ViewInject(R.id.text_view)
//        TextView mTextView;
//
//        TestViewHolder(View itemView) {
//            super(itemView);
//            x.view().inject(this, itemView);
//        }
//
//        private void bindData(String text) {
//            mTextView.setText(text);
//        }
//    }
}
