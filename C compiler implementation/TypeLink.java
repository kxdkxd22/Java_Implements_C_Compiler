public class TypeLink {
    boolean isDeclarator = true;
    boolean isTypeDef = false;

    Object typeObject;
    private TypeLink next = null;

    public TypeLink(boolean isDeclarator,boolean isTypeDef,Object typeObject){
        this.isDeclarator = isDeclarator;
        this.isTypeDef = isTypeDef;
        this.typeObject = typeObject;
    }

    public Object getTypeObject(){
        return typeObject;
    }

    public TypeLink toNext(){
        return next;
    }

    public void setNextLink(TypeLink obj){this.next = obj;}
}
