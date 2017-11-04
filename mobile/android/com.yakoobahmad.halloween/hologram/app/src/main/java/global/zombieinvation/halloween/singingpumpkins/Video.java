package global.zombieinvation.halloween.singingpumpkins;

/**
 * Created by yakoobahmad on 10/16/16.
 */
class Video {

    enum Name {WOODS,GRIM_GRINNING_GHOST,KIDNAP_SANDY_CLAWS,MONSTER_MASH,THIS_IS_HALLOWEEN,WHATS_THIS,OOGIE_BOOGIE_PUMPKINS,TIMEWARP,SAM_NOCOSTUME,SAM_SYMPHONY,SAM_SCARE1,SAM_SCARE2,SAM_SCARE3,SAM_SCARE4,BONEYARD_BAND,BONEYARD_PUMPKIN}

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
