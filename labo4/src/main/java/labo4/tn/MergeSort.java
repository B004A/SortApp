package labo4.tn;

import java.util.Arrays;

public class MergeSort extends SortTemplate {

    private void merge(int[] array, int start, int mid, int fin) {
        int nbElementsGauche = mid - start + 1;
        int nbElementsDroite = fin - mid;
        int[] partieGauche = new int[nbElementsGauche];
        int[] partieDroite = new int[nbElementsDroite];
        System.arraycopy(array, start, partieGauche, 0, nbElementsGauche);
        System.arraycopy(array, mid + 1, partieDroite, 0, nbElementsDroite);
        int i = 0;
        int j = 0;
        int k = start;
        while (i < nbElementsGauche && j < nbElementsDroite) {
            if (partieGauche[i] <= partieDroite[j]) {
                array[k] = partieGauche[i];
                i++;
            } else {
                array[k] = partieDroite[j];
                j++;
            }
            k++;
        }
        while (i < nbElementsGauche) {
            array[k] = partieGauche[i];
            i++;
            k++;
        }
        while (j < nbElementsDroite) {
            array[k] = partieDroite[j];
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
        return start + (fin - start) / 2;
    }

}
