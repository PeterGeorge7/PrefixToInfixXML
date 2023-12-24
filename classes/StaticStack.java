public class StaticStack<T> {
    private Object[] staticStack; // Array to store stack elements
    private int MAX_STACK_SIZE, // Maximum size of the stack
            topIndex; // Index of the top element in the stack

    // Constructor to initialize the stack with a maximum size
    public StaticStack(int MAX_STACK_SIZE) {
        this.MAX_STACK_SIZE = MAX_STACK_SIZE;
        this.staticStack = new Object[MAX_STACK_SIZE]; // Creating an array with the specified maximum size
        this.topIndex = -1; // Initializing topIndex to -1 since the stack is initially empty
    }

    // Method to push an element onto the stack
    public T push(T data) {
        if (isFull()) {
            System.out.println("Stack is full"); // Displaying a message if the stack is full
            return null; // Returning null as an indication of failure to push
        } else {
            this.topIndex++; // Incrementing topIndex
            this.staticStack[this.topIndex] = data; // Adding the element to the stack
            return (T) this.staticStack[topIndex]; // Returning the element that was pushed
        }
    }

    // Method to pop an element from the stack
    public T pop() {
        if (isEmpty()) {
            System.out.println("Stack is Empty"); // Displaying a message if the stack is empty
            return null; // Returning null as an indication of failure to pop
        } else {
            T popData = (T) this.staticStack[this.topIndex]; // Retrieving the top element
            this.staticStack[this.topIndex] = null; // Setting the top element to null
            this.topIndex--; // Decrementing topIndex
            return (T) popData; // Returning the popped element
        }
    }

    // Method to get the top element of the stack without removing it
    public Object topData() {
        return this.staticStack[this.topIndex];
    }

    // Method to check if the stack is full
    public Boolean isFull() {
        return ((this.topIndex + 1) == this.MAX_STACK_SIZE);
    }

    // Method to check if the stack is empty
    public Boolean isEmpty() {
        return (this.topIndex == -1);
    }

    public int stackSize() {
        return (this.topIndex + 1);
    }
}