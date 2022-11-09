package com.github.jdsjlzx.interfaces;

import android.view.View;

public interface IRefreshFooter {

	/**
	 * 处于可以下拉的状态，已经过了指定距离
	 */
	void onPrepare();

	/**
	 * 正在下拉
	 */
	void onChanging();

	/**
	 * 下拉移动
	 */
	void onMove(float offSet, float sumOffSet);

	/**
	 * 下拉松开
	 */
	boolean onRelease();

	/**
	 * 获取FootView
	 */
	View getFootView();

	/**
	 * 状态回调，加载中
	 */
	void onLoading();

	/**
	 * 状态回调，加载完成
	 */
	void onComplete();

	/**
	 * 状态回调，已全部加载完成
	 */
	void onNoMore();
}