package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    static class Node{
        public int nodeValue=0;
        public Node child=null;
        public Node next=null;
        public boolean visit=false;
    }

    public static void main(String[] args) throws IOException {
        FileReader fr = new FileReader("C:\\Users\\Lab\\IdeaProjects\\alpha_beta_prunning\\src\\com\\company\\demo-2.txt");
        BufferedReader br = new BufferedReader(fr);
        int childNum= Integer.parseInt(br.readLine());
        int heightNum= Integer.parseInt(br.readLine());
        Queue<Integer> nodeValue = new LinkedList<>();
        String[] nodeValueStringList =br.readLine().split(",");
        for (String nodeValueString:nodeValueStringList) nodeValue.offer(Integer.parseInt(nodeValueString));
        //System.out.println(nodeValue.toString());
        Node root = treeBuild(childNum,heightNum,nodeValue);
        findRootValue(root);
        fr.close();
    }

    public static void findRootValue(Node root){
        int max=-999,min=999;
        System.out.println("\nroot:"+findValueWithMode(root,-999,999,true));
        treePruning(root);
    }

    public static void treePruning(Node root){
        if(root.child!=null){
            treePruning(root.child);
        }else{
            if(!root.visit) System.out.print(root.nodeValue+" ");
        }
        if(root.next!=null){
            treePruning(root.next);
        }
    }

    public static int findValueWithMode(Node node,int max,int min,boolean mode){ //odd=>false-min,even=true-max
        int getValue;
        if(node.nodeValue!=0) {
            System.out.print(node.nodeValue+" ");
            node.visit=true;
        }
        if((max!=-999 && min!=999) &&((!mode&&min<max)||(mode&&max>min))) return (mode)?min:max;

        if(node.child!=null){
            getValue = findValueWithMode(node.child,max,min,!mode);
            if(mode && min>getValue) min=getValue;
            if(!mode && max<getValue) max=getValue;
        }else{
            if(!((!mode&&min<node.nodeValue)||(mode&&max>node.nodeValue))){
                if(node.next!=null){
                    getValue=findValueWithMode(node.next,max,min,mode);
                    getValue = (mode &&getValue<node.nodeValue)||(!mode &&getValue>node.nodeValue)? getValue:node.nodeValue;
                    return getValue;
                }else{
                    return node.nodeValue;
                }
            }else{
                //System.out.println("\npruning- alpha:"+max+",beta:"+min);
                return node.nodeValue;
            }
        }
        if(node.next!=null){
            getValue = findValueWithMode(node.next,max,min,mode);
            if(mode && min> getValue) min=getValue;
            if(!mode && max<getValue) max=getValue;
            return (mode)?min:max;
        }else{
            return (mode)?min:max;
        }

    }

    public static Node treeBuild(int childNum,int heightNum,Queue<Integer> valueList){
        Node root = new Node();
        LinkedList<Node> parentNode= new LinkedList<>();
        LinkedList<Node> childNode= new LinkedList<>();
        parentNode.push(root);
        Node parent;
        for(int i=0;i<heightNum;i++){
            while(!parentNode.isEmpty()) {
                parent=parentNode.pop();
                Node leftNode=new Node();
                parent.child=leftNode;
                childNode.push(leftNode);
                if(i==heightNum-1) leftNode.nodeValue=valueList.poll();
                for (int j = 0; j < childNum - 1; j++) {
                    Node rightNode = new Node();
                    leftNode.next=rightNode;
                    childNode.push(rightNode);
                    leftNode=rightNode;
                    if(i==heightNum-1) rightNode.nodeValue=valueList.poll();
                }
                leftNode.next=null;
            }
            while(!childNode.isEmpty()) parentNode.push(childNode.pop());
        }
        return root;
    }
}
