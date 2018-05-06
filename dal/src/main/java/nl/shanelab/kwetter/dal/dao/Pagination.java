package nl.shanelab.kwetter.dal.dao;

import lombok.*;

import java.util.Collection;

@Builder
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class Pagination<T> {

    @Setter(AccessLevel.PACKAGE)
    private Collection<T> collection;

    @Setter(AccessLevel.PACKAGE)
    private int page;

    @Setter(AccessLevel.PACKAGE)
    private int requestedSize;

    @Setter(AccessLevel.PACKAGE)
    private int count;

    /**
     * Check if the pagination has a previous page
     *
     * @return boolean Returns whether or not the pagination has a previous page
     */
    public boolean hasPrevious() {
        return count > 0 && this.pages() > 1 && page > 0;
    }

    /**
     * Check if the pagination has a next page
     *
     * @return boolean Returns whether or not the pagination has a next page
     */
    public boolean hasNext() {
        int pages = this.pages();
        return pages > 1 && page < (pages - 1);
    }

    /**
     * Get the number of pages
     *
     * @return int Returns the amount of possible pages
     */
    public int pages() {
        return this.isEmpty() ? 0 : requestedSize >= count ? 1 : (int) Math.ceil((double)count / (double)requestedSize);
    }

    /**
     * Check if the result set was empty
     *
     * @return boolean Returns whether or not the result set was empty
     */
    public boolean isEmpty() {
        return collection == null || collection.size() <= 0;
    }
}

