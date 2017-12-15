package com.gonext.callreminder.utils.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.gonext.callreminder.R;


public class CustomRecyclerView extends RecyclerView
{

    private Context context;
    private View emptyView;
    private View  refeshLayoutView;
    private String emptyTitle ;
    private String emptyDescription;
    private String emptyTextOnButton;
    private int imageId;
    CustomButton btnEmpty;
    OnClickListener onClickListener;
    private View refreshLayoutView;

    /**
     * Constructor with 1 parameter context and attrs
     *
     * @param context  context
     */
    public CustomRecyclerView(Context context) {
        super(context);
    }

    /**
     * Constructor with 2 parameters context and attrs
     *
     * @param context context
     * @param attrs context
     */
    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initCustomText(context,attrs);
    }
    /**
     * Initializes all the attributes and respective methods are called based on the attributes
     *
     * @param context context of activity
     * @param attrs attrs
     */
    private void initCustomText(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomRecyclerView);

        int listOrientation = ta.getInt(R.styleable.CustomRecyclerView_list_orientation, 0);
        int listType = ta.getInt(R.styleable.CustomRecyclerView_list_type, 0);
        int grid_span = ta.getInt(R.styleable.CustomRecyclerView_gird_span, 3);

        /*
         * A custom getView uses isInEditMode() to determine whether or not it is being rendered inside the editor
         * and if so then loads test data instead of real data.
         */
        LayoutManager layoutManager = null;
        if (!isInEditMode()) {
            int layoutOrientation = 0;
            switch (listOrientation) {
                case 0:
                    layoutOrientation = OrientationHelper.VERTICAL;
                    break;
                case 1:
                    layoutOrientation = OrientationHelper.HORIZONTAL;
                    break;
                default:
                    layoutOrientation = OrientationHelper.VERTICAL;
            }

            switch (listType) {
                case 0:
                    layoutManager = new LinearLayoutManager(context, layoutOrientation, false);
                    break;
                case 1:
                    layoutManager = new GridLayoutManager(context, grid_span, layoutOrientation, false);
                    break;
                case 2:
                    layoutManager = new StaggeredGridLayoutManager(grid_span, layoutOrientation);
                    break;
                default:
                    layoutManager = new LinearLayoutManager(context, layoutOrientation, false);
                    break;
            }

            setLayoutManager(layoutManager);

            ta.recycle();
        }
    }



    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        checkIfEmpty();
    }

    public void setEmptyView(View emptyView,View refeshLayoutView) {
        this.refeshLayoutView = refeshLayoutView;
        setEmptyView(emptyView);
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            if (refeshLayoutView != null){
                refeshLayoutView.setVisibility(emptyViewVisible ? GONE : VISIBLE);
            }
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
            setDataInEmptyView();
        }
    }

    public void setEmptyData(String emptyTitle, String emptyDescription, String emptyTextOnButton, int imageId, OnClickListener onClickListener) {
        this.emptyTitle = emptyTitle;
        this.emptyDescription = emptyDescription;
        this.emptyTextOnButton = emptyTextOnButton;
        this.imageId = imageId;
        this.onClickListener = onClickListener;
        if (emptyView != null && emptyView.getVisibility() == VISIBLE) {
            setDataInEmptyView();
        }
    }

    public void setEmptyData(String emptyTitle, String emptyDescription, int imageId, OnClickListener onClickListener) {
        setEmptyData(emptyTitle,emptyDescription,"",imageId,onClickListener);
    }

    public void setEmptyData(String emptyTitle, int imageId) {
        setEmptyData(emptyTitle,"","",imageId,null);
    }

    public void setEmptyData(String emptyTitle, String emptyDescription) {
        setEmptyData(emptyTitle,emptyDescription,"",0,null);
    }

    public void setEmptyData(String emptyTitle, String emptyDescription, int imageId) {
        setEmptyData(emptyTitle,emptyDescription,"",imageId,null);
    }

    public void setEmptyData(String emptyTitle) {
        setEmptyData(emptyTitle,"","",0,null);

    }
    private void setDataInEmptyView() {
        CustomTextView tvTitle = (CustomTextView) emptyView.findViewById(R.id.tvEmptyTitle);
        CustomTextView tvDescription = (CustomTextView) emptyView.findViewById(R.id.tvEmptyDescription);
        ImageView ivImage = (ImageView) emptyView.findViewById(R.id.ivEmptyImage);
        btnEmpty = (CustomButton) emptyView.findViewById(R.id.btnEmpty);


        Animation animationTitle = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        Animation animationDesc = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        Animation animationImage = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);
        Animation animationButton = AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in);

        tvDescription.setVisibility(TextUtils.isEmpty(emptyDescription)?GONE :VISIBLE);
        tvTitle.setVisibility(TextUtils.isEmpty(emptyTitle)?GONE :VISIBLE);
        btnEmpty.setVisibility(TextUtils.isEmpty(emptyTextOnButton)?GONE :VISIBLE);
        ivImage.setVisibility(imageId <= 0 ? GONE :VISIBLE);

        try {
            tvTitle.startAnimation(animationTitle);
            tvDescription.startAnimation(animationDesc);
            btnEmpty.startAnimation(animationButton);
            if (ivImage.getVisibility() == VISIBLE) {
                ivImage.setImageResource(imageId);
                ivImage.startAnimation(animationImage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvTitle.setText(emptyTitle);
        tvDescription.setText(emptyDescription);
        btnEmpty.setText(emptyTextOnButton);

        if (ivImage.getVisibility() == VISIBLE) {
            ivImage.setImageResource(imageId);
            ivImage.startAnimation(animationImage);
        }
        btnEmpty.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
            }
        });

    }

}
