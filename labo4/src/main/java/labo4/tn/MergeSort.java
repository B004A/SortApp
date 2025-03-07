package labo4.tn;

import java.util.Arrays;

public class MergeSort extends SortTemplate {
    public MergeSort(App sortApp) {
        this.sortApp = sortApp;
    }

    private void merge(int[] array, int start, int mid, int fin) {
        int nbLeftElements = mid - start + 1;
        int nbRightElements = fin - mid;
        int[] leftPart = new int[nbLeftElements];
        int[] rightPart = new int[nbRightElements];
        System.arraycopy(array, start, leftPart, 0, nbLeftElements);
        System.arraycopy(array, mid + 1, rightPart, 0, nbRightElements);
        int i = 0;
        int j = 0;
        int k = start;
        while (i < nbLeftElements && j < nbRightElements) {
            if (leftPart[i] <= rightPart[j]) {
                array[k] = rightPart[i];
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
        int[] splitPointArray = { array[start + (fin - start) / 2] };
        sleepThread(sleepTime, splitPointArray);
        return start + (fin - start) / 2;
    }

}
