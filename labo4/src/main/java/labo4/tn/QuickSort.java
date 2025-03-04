package labo4.tn;

public class QuickSort extends SortTemplate {

    private void changerLesElements(int[] array, int indexI, int indexJ) {
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

    @Override
    public int findSplitPoint(int[] array, int start, int fin) {
        int pivot = array[fin];
        int indexPlusPetit = start - 1;
        for (int j = start; j <= fin; j++) {
            if (array[j] < pivot) {
                indexPlusPetit++;
                changerLesElements(array, indexPlusPetit, j);
            }
        }
        changerLesElements(array, indexPlusPetit + 1, fin);
        return indexPlusPetit + 1;

    }
}
