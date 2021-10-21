package com.blackjack;

/*
手牌类
 */
public class Hand {

    private Card handCards[];  //Card类型数组
    private int num;           //记录已经发给的纸牌张数。

    /*
    构造函数
     */
    public Hand() {
        handCards = new Card[5];  //手牌最多不会超过5张
        for (int i = 0; i < 5; i++) handCards[i] = new Card();
        num = 0;
    }

    /*
    Post: 接受发的一张牌，即把一张牌加入handCards数组中
     */
    public void addHandCards(Card c) {
        handCards[num] = c;
        num++;
    }

    /*
    Post: 返回手牌
     */
    public Card[] getHandCards(){
        return handCards;
    }

    /*
    Post: 返回手牌的数量
     */
    public int getNum() {
        return num;
    }

    /*
    Post: 清空手牌
     */
    public void clearHand() {
        num = 0;
    }
}

