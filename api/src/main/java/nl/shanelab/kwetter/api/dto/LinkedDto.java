package nl.shanelab.kwetter.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import nl.shanelab.kwetter.api.hateoas.Linked;

import java.util.Collection;
import java.util.HashSet;

public abstract class LinkedDto {
    interface LinkedDelegate {
        boolean add(Linked item);
        boolean remove(Linked item);
    }

    @Delegate(types = LinkedDelegate.class)
    @Getter
    @Setter
    private Collection<Linked> links = new HashSet<>();
}
