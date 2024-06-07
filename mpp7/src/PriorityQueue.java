public class PriorityQueue {
    private final Node[] heap;
    private int pointer;

    public PriorityQueue(Node[] fileData) {
        heap = fileData;
        pointer = heap.length;

        int lastInnerNode = getParentIndex(pointer - 1);
        for (int i = lastInnerNode; i >= 0; i--) {
            fixDown(i);
        }
    }

    public void insert(Node nodeToInsert) {
        heap[pointer] = nodeToInsert;
        fixUp(pointer);
        pointer++;
    }

    public int size() {
        return pointer;
    }

    public Node getMin() {
        if (heap.length > 0) {
            return heap[0];
        }
        return null;
    }

    public void delMin() {
        pointer--;
        heap[0] = heap[pointer];
        heap[pointer] = null;

        fixDown(0);
    }

    // Naprawia drzewo w górę, zaczynają od podanego indeksu
    private void fixUp(int index) {
        int parentIndex = getParentIndex(index);
        while (index > 0 && getWeightOf(index) < getWeightOf(parentIndex)) {
            Node tmp = heap[index];
            heap[index] = heap[parentIndex];
            heap[parentIndex] = tmp;

            index = parentIndex;
            parentIndex = getParentIndex(index);
        }
    }

    // Naprawia heap w doł, zaczynając zawsze od podanego indeksu
    private void fixDown(int index) {
        while (index < pointer) {
            int leftChildIndex = getLeftIndex(index);
            int rightChildIndex = getRightIndex(index);

            // Sprawdzamy, czy element ma jakiekolwiek dzieci
            if (leftChildIndex >= pointer)
                break;

            // Wybieramy mniejsze dziecko
            int toSwapIndex;
            if (rightChildIndex >= pointer) { // Jeśli ma tylko lewe dziecko, to jest ono większe
                toSwapIndex = leftChildIndex;
            } else if (getWeightOf(leftChildIndex) >= getWeightOf(rightChildIndex)) { // Jeśli prawe dziecko jest większe
                toSwapIndex = rightChildIndex;
            } else { // Jeśli lewe jest większe bądź równe
                toSwapIndex = leftChildIndex;
            }

            // Jeśli mniejsze dziecko jest większe od rodzica, to znaczy,
            // że heap jest już poprawny
            if (getWeightOf(index) <= getWeightOf(toSwapIndex)) {
                break;
            }

            // W przeciwnym wypadku zamieniamy rodzica z mniejszym dzieckiem
            Node tmp = heap[index];
            heap[index] = heap[toSwapIndex];
            heap[toSwapIndex] = tmp;
            index = toSwapIndex;
        }
    }

    private int getWeightOf(int index) {
        return heap[index].getWeight();
    }

    // Zwraca lewe dziecko elementu na podanym indeksie
    private int getLeftIndex(int index) {
        return 2 * index + 1;
    }

    // Zwraca prawe dziecko elementu na podanym indeksie
    private int getRightIndex(int index) {
        return 2 * index + 2;
    }

    // Zwraca rodzica elementu na podanym indeksie
    private int getParentIndex(int index) {
        return (index - 1) / 2;
    }
}
