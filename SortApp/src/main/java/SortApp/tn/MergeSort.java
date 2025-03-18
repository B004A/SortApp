package SortApp.tn;

public class MergeSort extends SortTemplate {
    public MergeSort(App sortApp) {
        super(sortApp);
    }

    // merge 2 sub arrays back together
    private void merge(int[] array, int start, int mid, int fin) {
        if (!running || Thread.currentThread().isInterrupted()) {
            return; // Exit immediately if the thread is interrupted or stopped
        }
        int nbLeftElements = mid - start + 1;
        int nbRightElements = fin - mid;
        int[] leftPart = new int[nbLeftElements];
        sleepThread(sleepTime, leftPart);
        int[] rightPart = new int[nbRightElements];
        System.arraycopy(array, start, leftPart, 0, nbLeftElements);
        System.arraycopy(array, mid + 1, rightPart, 0, nbRightElements);
        int[] mergeElements = new int[nbLeftElements + nbRightElements];
        System.arraycopy(leftPart, 0, mergeElements, 0, nbLeftElements);
        System.arraycopy(rightPart, 0, mergeElements, nbLeftElements, nbRightElements);
        sleepThread(sleepTime, mergeElements);
        int i = 0;
        int j = 0;
        int k = start;
        while (i < nbLeftElements && j < nbRightElements) {
            if (leftPart[i] <= rightPart[j]) {
                array[k] = leftPart[i];
                i++;
            } else {
                array[k] = rightPart[j];
                j++;
            }
            k++;
        }
        while (i < nbLeftElements) {
            array[k] = leftPart[i];
            i++;
            k++;
        }
        while (j < nbRightElements) {
            array[k] = rightPart[j];
            j++;
            k++;
        }
    }

    @Override
    public void sort(int[] array, int start, int fin) {
        if (!running || Thread.currentThread().isInterrupted()) {
            return; // Exit immediately if the thread is interrupted or stopped
        }
        if (start >= fin) {
            return;
        }
        int splitPoint = findSplitPoint(array, start, fin);
        sort(array, start, splitPoint);
        sort(array, splitPoint + 1, fin);
        merge(array, start, splitPoint, fin);
    }

    @Override
    public int findSplitPoint(int[] array, int start, int fin) {
        if (!running || Thread.currentThread().isInterrupted()) {
            return fin; // Exit immediately if the thread is interrupted or stopped
        }
        int[] splitPointArray = { array[start + (fin - start) / 2] };
        sleepThread(sleepTime, splitPointArray);
        return start + (fin - start) / 2;
    }

}
