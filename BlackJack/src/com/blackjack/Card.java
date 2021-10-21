package com.blackjack;

/*
卡牌类
 */
public class Card {

    private int point;
    private String face;  //面值
    private String suit;  //花色

    public static String FACES[] = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
    public static String SUITS[] = {"红桃", "黑桃", "方块", "梅花"};

    /*
    无参构造函数
    开始游戏后需要新建Card，因为需要打乱后发牌，此时的牌不能确定，所以用无参构造
     */
    public Card() {
        point = 0;
        face = "";
        suit = "";
    }

    /*
    有参数构造函数，一副牌只有52张
     */
    public Card(int n) {
        if(n <= 52 && n > 0) {
            face = FACES[n % FACES.length];
            suit = SUITS[n % SUITS.length];

            //1或者11的设置在BlackJack类中
            if(face == "A") { point = 11; }
            else if(face == "2") { point = 2; }
            else if(face == "3") { point = 3; }
            else if(face == "4") { point = 4; }
            else if(face == "5") { point = 5; }
            else if(face == "6") { point = 6; }
            else if(face == "7") { point = 7; }
            else if(face == "8") { point = 8; }
            else if(face == "9") { point = 9; }
            else{ point = 10; }
        }
        else { System.exit(0); }
    }

    /*
    Post: 返回花色
     */
    public String getSuit() {
        return suit;
    }

    /*
    Post: 返回面值
     */
    public String getFace() {
        return face;
    }

    /*
    Post: 返回点数
     */
    public int getPoint() {
        return point;
    }
}

