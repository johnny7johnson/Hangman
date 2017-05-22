package com.example.johanna.hangman;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Johanna on 09.01.2017.
 */

public class HangmanView extends View {

    private Path mPath;
    private Paint myPaint;
    private Canvas mCanvas;
    private HangmanActivity hangmanAct;
    private ArrayList<Point> points;
    private int width;
    private int height;
    private RelativeLayout layout;
    private ArrayList<TextView> textViews;
    private Character c;
    private int failNr;

    public HangmanView(Context context) {
        super(context);
        failNr = 0;
        hangmanAct = (HangmanActivity) context;
        mPath = new Path();
        points = new ArrayList<>();
        width = hangmanAct.getWindowManager().getDefaultDisplay().getWidth();
        height = hangmanAct.getWindowManager().getDefaultDisplay().getHeight();
        initPoints();
        textViews = new ArrayList<>();

    }


    //@RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        initPaint();
        if (textViews.size() == 0 || hangmanAct.getRightNr() == 0) {
            textViews = new ArrayList<>();
            drawEmptyUnderlines(hangmanAct.getLength());
        }
        if (textViews.size() == 0 && !hangmanAct.getDiff().equals("simple")) {
            drawEmptyUnderlines(hangmanAct.getLength());
        }
        Character letter = hangmanAct.getLetter();
        if (letter != null) {
            ArrayList<Integer> positions = hangmanAct.getPositions();
            if (positions != null) {
                drawLetters(positions, hangmanAct.getLetter());
            }
        }
        failNr = hangmanAct.getFailNr();
        if (failNr == 5) {
            drawHang();
        }
        drawNext(failNr);


        hangmanAct.checkWin();
    }


    /**
     * initializes Paint for drawing on Canvas
     **/
    public void initPaint() {
        myPaint = new Paint();
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setColor(Color.WHITE);
        mCanvas.drawPaint(myPaint);
        myPaint.setColor(Color.BLACK);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeJoin(Paint.Join.ROUND);
        if (width < 1024) {
            myPaint.setStrokeWidth(8f);
        } else {
            myPaint.setStrokeWidth(10f);
        }
    }


    /*******************************************************************************
     * draw word
     *******************************************************************************/

    /**
     * init Array out of textviews with underlines. One textview for every letter of the word.
     **/
    public void drawEmptyUnderlines(int wordLength) {
        layout = new RelativeLayout(hangmanAct);
        String underline = "__";
        Point underlinePos;
        int hoehenzaehler = 1;
        int breitenzaehler = 1;

        for (int i = 0; i < wordLength; i++) {
            float underlineX = 100 * breitenzaehler;
            float underlineY = 850 + 100 * hoehenzaehler;
            underlinePos = new Point(this, underlineX, underlineY);
            float breite = breitenzaehler * convertX(100);
            float maxBreite = width - 200;
            if (breite < maxBreite) {
                makeTextView(underlinePos, underline);
            } else {
                hoehenzaehler++;
                breitenzaehler = 0;
                makeTextView(underlinePos, underline);
            }
            breitenzaehler++;
        }

        Point instructionPos = new Point(this, 320, 1350);
        makeInstructionTextView(instructionPos, "Tap to try a new letter");
        makeEditableFields();
        layout.measure(mCanvas.getWidth(), mCanvas.getHeight());
        layout.layout(0, 0, mCanvas.getWidth(), mCanvas.getHeight());

        layout.draw(mCanvas);

    }

    /**
     * creates TextView with instruction for player
     **/
    private void makeInstructionTextView(Point position, String content) {
        TextView textView = new TextView(hangmanAct);
        textView.setVisibility(View.VISIBLE);
        textView.setText(content);
        textView.setTextSize(16f);
        textView.setMinWidth((int) convertX(150));
        textView.setX(position.getX());
        textView.setY(position.getY());
        layout.addView(textView);

    }

    /**
     * creates a Textview and add it to Array
     *
     * @param position: position in view on wich it will be printed
     * @param content:  string wich will be printed
     **/
    private void makeTextView(Point position, String content) {
        TextView textView = new TextView(hangmanAct);

        textView.setVisibility(View.VISIBLE);
        textView.setText(content);
        textView.setTextSize(28f);
        textView.setMinWidth((int) convertX(150));
        textView.setX(position.getX());
        textView.setY(position.getY());
        textViews.add(textView);
        layout.addView(textView);

    }

    /**
     * prints a letter instead of underline
     *
     * @param pos:    positions in word on wich given letter is
     * @param letter: chosen letter wich belongs to the word
     **/
    public void drawLetters(ArrayList<Integer> pos, Character letter) {
        for (int i = 0; i < pos.size(); i++) {
            TextView textView = textViews.get(pos.get(i));
            layout.removeView(textView);
            textView.setText(letter.toString());    //hier!
            textView.setWidth((int) convertX(200));
            textView.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(textView);
            layout.draw(mCanvas);
        }
    }


    /**************************************************
     * draw rest of layout
     *****************************************/

    /**
     * listener for whole screen. On touch a alert dialog will be opened
     **/
    private void makeEditableFields() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                ArrayList<Character> checkedLetters = hangmanAct.getCheckedLetters();
                String checkedLettersPretty = "";
                for (int i = 0; i < checkedLetters.size(); i++) {
                    checkedLettersPretty = checkedLettersPretty + checkedLetters.get(i).toString() + "  ";
                }

                final EditText input = new EditText(hangmanAct);
                input.setHint("yourLetter");
                input.setTextSize(20f);


                AlertDialog.Builder builder = new AlertDialog.Builder(hangmanAct);
                builder.setMessage("You have already tried these letters:\n" + checkedLettersPretty + "\n\nPlease try another one ")
                        .setTitle("Choose");

                builder.setView(input);

                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String inputLetter = input.getText().toString();
                        if (inputLetter.length() != 0) {
                            c = inputLetter.toCharArray()[0];                       //falls nichts eingegeben: index out of bounds
                            hangmanAct.handleNewLetter(c);
                        } else {
                            Toast.makeText(hangmanAct, "Please give me a letter", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }

        });
    }


    /*********************************************************
     * draw hangman paint
     ********************************************************/

    /**
     * @param failNr dependig on Number of Fails the next part of hangman will be drawn
     **/
    public void drawNext(int failNr) {

        switch (failNr) {
            case 0:
                //fuss links
                break;
            case 1:
                //fuss rechts
                connectPoints(13, 14);
                break;
            case 2:
                //balken hoch
                connectPoints(11, 12);
                break;
            case 3:
                //balken rechts
                connectPoints(10, 11);
                break;
            case 4:
                //balken diagonal
                connectPoints(15, 16);
                break;
            case 5:
                //seil
                connectPoints(9, 10);
                break;
            case 6:
                //kopf
                for (int i = 0; i < failNr; i++) {
                    drawNext(i);
                }
                drawHead();

                break;
            case 7:
                //körper
                drawHead();
                connectPoints(2, 3);
                break;
            case 8:
                //arm links
                drawHead();
                connectPoints(4, 5);
                break;
            case 9:
                //arm rechts
                drawHead();
                connectPoints(4, 6);
                break;
            case 10:
                //bein links
                drawHead();
                connectPoints(3, 7);
                break;
            case 11:
                //bein rechts
                drawHead();
                connectPoints(3, 8);
                hangmanAct.playerLost();

                break;
        }
    }


    /**
     * @param begin
     * @param end   draws line from begin to end
     */
    public void connectPoints(int begin, int end) {
        mPath.moveTo(points.get(begin).getX(), points.get(begin).getY());
        mPath.lineTo(points.get(end).getX(), points.get(end).getY());
        mCanvas.drawPath(mPath, myPaint);
    }


    /**
     * creates array of points on canvas
     **/
    public void initPoints() {
        int x = getWidth();
        int y = getHeight();
        Point middle = new Point(this, width / 2, height / 2);
        points.add(middle);                                //0, middle
        Point point = new Point(this, 700, 420);          //1, center of head
        points.add(point);
        point = new Point(this, 700, 480);                //2, begin of body
        points.add(point);
        point = new Point(this, 700, 700);                //3, end of body
        points.add(point);
        point = new Point(this, 700, 550);                //4, begin of arms
        points.add(point);
        point = new Point(this, 620, 600);                //5, end of left arm
        points.add(point);
        point = new Point(this, 780, 600);                //6, end of right arm
        points.add(point);
        point = new Point(this, 620, 800);                //7, end of left leg
        points.add(point);
        point = new Point(this, 780, 800);                //8, end of right leg
        points.add(point);
        point = new Point(this, 700, 360);                //9, top of head
        points.add(point);
        point = new Point(this, 700, 200);                //10, begin of rope
        points.add(point);
        point = new Point(this, 250, 200);                //11, upper left corner
        points.add(point);
        point = new Point(this, 250, 900);                //12, downer left T-corner
        points.add(point);
        point = new Point(this, 100, 900);                //13, down left foot of hang
        points.add(point);
        point = new Point(this, 550, 900);                //14, down right foot of hang
        points.add(point);
        point = new Point(this, 400, 200);               //15,upper end of diagonal
        points.add(point);
        point = new Point(this, 250, 350);               //16, right end of diagonal
        points.add(point);

    }


    /**
     * draws head of hangman
     **/
    private void drawHead() {
        float radius;
        radius = 60;
        Point p = new Point(this, 0, 80);
        radius = radius * height / 1536;
        mCanvas.drawCircle(points.get(1).getX(), points.get(1).getY(), radius, myPaint);
    }

    public void drawHang() {
        for (int i = 0; i < 6; i++) {
            failNr = i;
            drawNext(failNr);
        }
        failNr = 5;
    }


    /**
     * coding outgoing from screen resolution 1080 x 1536
     * converts x element my code pixel to related pixel in real resolution
     *
     * @param x coding pixel element
     * @return real pixel element
     **/
    private float convertX(float x) {
        float mX = x * width / 1080;
        return mX;
    }

    /**
     * converts y element my code pixel to related pixel in real resolution
     *
     * @param y coding pixel element
     * @return real pixel element
     **/
    private float convertY(float y) {
        float mY = y * height / 1536;
        return mY;
    }

    public int getDisplyWidth() {
        return width;
    }

    public int getDisplayHeight() {
        return height;
    }

    public ArrayList getTextViewList() {
        return textViews;
    }
}
