package org.jbibtex.policies.keyconflictresolution;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.DuplicateKeyException;
import org.jbibtex.KeyMap;
import org.jbibtex.policies.BibTeXEntryKeyConflictResolutionPolicy;

public class ThrowOnDuplicate implements BibTeXEntryKeyConflictResolutionPolicy {
    @Override
    public BibTeXEntry entryToPut(BibTeXEntry entryCandidate, KeyMap<BibTeXEntry> currentEntries) {
        if(currentEntries.containsKey(entryCandidate.getKey())) {
            throw new DuplicateKeyException(entryCandidate.getKey());
        }

        return entryCandidate;
    }
}
