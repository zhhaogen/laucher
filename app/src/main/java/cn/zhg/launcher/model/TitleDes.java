package cn.zhg.launcher.model;
/**
 * 标题和描述
 */
public class TitleDes
{
    public TitleDes(){

    }
    public TitleDes(String title,Object des){
        this.title=title;
        if(des==null){
            this.des=null;
        }else{
            this.des=des.toString();
        }
    }
    public String title;
    public String des;
}