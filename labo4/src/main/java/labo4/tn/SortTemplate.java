package labo4.tn;

public abstract class SortTemplate {

    public void sort(int[] array, int start, int fin) {
        if (start >= fin) {
            return;
        }
        int splitPoint = findSplitPoint(array, start, fin);
        sort(array, start, splitPoint);
        sort(array, splitPoint, fin);
    }

    public int findSplitPoint(int[] array, int start, int fin) {
        return 0;
    }
}
