package backend;

import frontend.CTokenType;

import java.util.*;

public class ICodeNodeImpl extends HashMap<ICodeKey,Object> implements ICodeNode{

    private CTokenType type;
    private ICodeNode parent;
    private ArrayList<ICodeNode> children;

    public ICodeNodeImpl(CTokenType type){
        this.type = type;
        this.parent = null;
        this.children = new ArrayList<ICodeNode>();
        setAttribute(ICodeKey.TokenType,type);
    }


    @Override
    public ICodeNode getParent() {
        return parent;
    }

    @Override
    public ICodeNode addChild(ICodeNode node) {
        if(node!=null){
            children.add(node);
            ((ICodeNodeImpl)node).parent = this;
        }
        return node;
    }

    @Override
    public ArrayList<ICodeNode> getChildren() {
        return children;
    }

    @Override
    public void setAttribute(ICodeKey key, Object value) {
        put(key,value);
    }

    @Override
    public Object getAttribute(ICodeKey key) {
        return get(key);
    }

    @Override
    public ICodeNode copy() {
        ICodeNodeImpl copy = (ICodeNodeImpl) ICodeFactory.createICodeNode(type);
        Set<Map.Entry<ICodeKey, Object>> attributes =  entrySet();
        Iterator <Map.Entry<ICodeKey,Object>> it = attributes.iterator();

        while(it.hasNext()){
            Map.Entry<ICodeKey,Object> attribute = it.next();
            copy.put(attribute.getKey(),attribute.getValue());
        }

        return copy;
    }

    public String toString(){
        return type.toString();
    }
}
