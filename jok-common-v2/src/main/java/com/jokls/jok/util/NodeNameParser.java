package com.jokls.jok.util;

public class NodeNameParser {
    private String nodeName = null;
    private int nodeNo = -1 ;

    public NodeNameParser(String identityName){
        this.parse(identityName);
    }

    private void parse(String identityName) {
        if(identityName != null && identityName.length() !=0 ){
            int slashIndex = identityName.lastIndexOf("#");
            if(slashIndex == -1){
                this.nodeName = identityName;
                this.nodeNo = -1;
            }else{
                String name = identityName.substring(0, slashIndex);
                String no = identityName.substring(slashIndex + 1);

                try{
                    this.nodeNo = StringUtil.toInt(no);
                    this.nodeName = name;
                }catch ( Exception e){
                    this.nodeName = identityName;
                    this.nodeNo = -1;
                }
            }
        }
    }

    public String getNodeName() {
        return this.nodeName;
    }

    public int getNodeNo() {
        return this.nodeNo;
    }
}
