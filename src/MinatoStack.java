class Node<E> {
    Node<E> previous;
    E current;
    Node<E> next;
} // class Node<E>

//=============================================
public class MinatoStack<E> {
    Node<E> dummy;
    Node<E> lastNode;

    //=================================
    //Constructor
    public MinatoStack() {
	dummy = new Node<E>();
	dummy.previous = null;
	dummy.current = null;
	dummy.next = null;
	lastNode = dummy;
    } // MinatoStack()

    //===================================
    public void push(E toAdd) {
	Node<E> newNode = new Node<E>();
	newNode.current = toAdd;
	newNode.previous = lastNode;
	newNode.previous.next = newNode;
	lastNode = newNode;
    } // push()

    //===================================
    public boolean empty() {
	if(lastNode == dummy) {
	    return true;
	}
	else {
	    return false;
	}
    } // empty()

    //====================================
    public E pop() {
	if(this.empty()) {
	    return null;
	}

	E toReturn = lastNode.current;
	lastNode = lastNode.previous;
	return toReturn;
    } // pop()
} // class MinatoStack



