package labo4.tn;

public class QuickSort extends SortTemplate implements Runnable {

    public QuickSort(App sortApp) {
        this.sortApp = sortApp;
    }

    private void swapElements(int[] array, int indexI, int indexJ) {
        int temp = array[indexJ];
        array[indexJ] = array[indexI];
        array[indexI] = temp;
    }

    @Override
    public void sort(int[] array, int start, int fin) {
        if (start >= fin) {
            return;
        }
        int splitPoint = findSplitPoint(array, start, fin);
        sort(array, start, splitPoint - 1);
        sort(array, splitPoint + 1, fin);
    }

    // find pivot index and return it
    @Override
    public int findSplitPoint(int[] array, int start, int fin) {
        int pivot = array[fin];
        int[] elementsToSwap = new int[3];
        int[] arrPivot = { array[fin] };
        sleepThread(sleepTime, arrPivot);
        int lowestIndex = start - 1;
        // highligth elements being sorted
        for (int j = start; j <= fin; j++) {
            if (array[j] < pivot) {
                lowestIndex++;
                elementsToSwap[0] = array[lowestIndex];
                elementsToSwap[1] = array[j];
                elementsToSwap[2] = array[fin];
                sleepThread(sleepTime, elementsToSwap);
                swapElements(array, lowestIndex, j);
                sleepThread(sleepTime, elementsToSwap);
            }
        }
        elementsToSwap[0] = array[lowestIndex + 1];
        elementsToSwap[1] = array[fin];
        sleepThread(sleepTime, elementsToSwap);
        swapElements(array, lowestIndex + 1, fin);
        sleepThread(sleepTime, elementsToSwap);

        return lowestIndex + 1;

    }

}
