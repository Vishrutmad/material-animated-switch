package com.github.glomadrian.switchinboxpinned.view.painter;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.github.glomadrian.switchinboxpinned.view.SwitchInboxPinedState;
import com.github.glomadrian.switchinboxpinned.view.observer.BallMoveObservable;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Adrián García Lomas
 */
public class BasePainter implements SwitchInboxPinnedPainter, Observer {

  private Paint paint;
  private int bgColor;
  private int toBgColor;
  private int pading;
  private int height;
  private int width;
  private Paint toBgPainter;
  private ValueAnimator colorAnimator;

  public BasePainter(int bgColor, int toBgColor, int pading) {
    this.bgColor = bgColor;
    this.toBgColor = toBgColor;
    this.pading = pading;
    init();
  }

  private void init() {
    paint = new Paint();
    paint.setColor(bgColor);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint.setAntiAlias(true);

    toBgPainter = new Paint();
    toBgPainter.setColor(toBgColor);
    toBgPainter.setStrokeCap(Paint.Cap.ROUND);
    toBgPainter.setAntiAlias(true);
    toBgPainter.setAlpha(0);

    initColorAnimator();
    BallMoveObservable.getInstance().addObserver(this);
  }

  private void initColorAnimator() {
    colorAnimator = ValueAnimator.ofInt(0, 255);
    colorAnimator.setDuration(100 - 35);
    colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
        toBgPainter.setAlpha((Integer) animation.getAnimatedValue());
      }
    });
  }

  @Override public void draw(Canvas canvas) {
    canvas.drawLine(pading, height / 2, width - pading, height / 2, paint);
    canvas.drawLine(pading, height / 2, width - pading, height / 2, toBgPainter);
  }

  @Override public int getColor() {
    return bgColor;
  }

  @Override public void setColor(int color) {

  }

  @Override public void onSizeChanged(int height, int width) {
    this.height = height;
    this.width = width;
    paint.setStrokeWidth(height / 2);
    toBgPainter.setStrokeWidth(height / 2);
  }

  @Override public void setState(SwitchInboxPinedState state) {

  }

  @Override public void update(Observable observable, Object data) {
    //TODO better pls
    int value = ((BallMoveObservable) observable).getBallPosition();
    int finalValue = value - 35;
    colorAnimator.setCurrentPlayTime(finalValue);
  }
}
