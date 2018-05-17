package org.wonday.pdf;

/**
 * Created by muralids on 5/16/18.
 */

public interface PdfLoadActions {

        public void pdfLoadFinished();
        public void onPageChanged(int page,int totalPages);
}
