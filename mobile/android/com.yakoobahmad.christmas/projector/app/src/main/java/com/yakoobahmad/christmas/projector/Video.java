package com.yakoobahmad.christmas.projector;

class Video {

    enum Name {DECK_THE_HALLS, GREAT_GIFT_WRAP, MARCH_WOODEN_SOLDIER, PACKING_SANTA_SLEIGH, TOY_TINKERING, NBC1, NBC2}

    private Name name;
    private String command;
    private String event;

    public Name getName(){
        return this.name;
    }

    public void setName(Name n){
        this.name = n;
    }

    public String getCommand(){
        return this.command;
    }
    public void setCommand(String c){
        this.command = c;
    }

    public String getEvent(){ return this.event; }
    public void setEvent(String e) { this.event = e; }

}
