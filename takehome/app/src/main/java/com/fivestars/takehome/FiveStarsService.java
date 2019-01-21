package com.fivestars.takehome;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class FiveStarsService extends CordovaService {

    @Override
    public void onCreate() {
        super.onCreate();

        ImageView closeButton = (ImageView) contentView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        final ImageView chatHeadImage = (ImageView) contentView.findViewById(R.id.move_btn);
        chatHeadImage.setOnTouchListener(new View.OnTouchListener() {
            private int lastAction;
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();

                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_UP:
                        //As we implemented on touch listener with ACTION_MOVE,
                        //we have to check if the previous action was ACTION_DOWN
                        //to identify if the user clicked the view or not.
//                        if (lastAction == MotionEvent.ACTION_DOWN) {
//                            //Open the chat conversation click.
//                            Intent intent = new Intent(ChatHeadService.this, ChatActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//
//                            //close the service and remove the chat heads
//                            stopSelf();
//                        }
                        lastAction = event.getAction();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(contentView, params);
                        lastAction = event.getAction();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    int getLayoutToInflate() {
        return R.layout.service_five_stars;
    }

    @Override
    int getWebViewParentLayoutId() {
        return R.id.five_stars_frame_layout;
    }
}
