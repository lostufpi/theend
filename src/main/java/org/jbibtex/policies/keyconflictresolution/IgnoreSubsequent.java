package org.jbibtex.policies.keyconflictresolution;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.KeyMap;
import org.jbibtex.policies.BibTeXEntryKeyConflictResolutionPolicy;

public class IgnoreSubsequent implements BibTeXEntryKeyConflictResolutionPolicy {
    @Override
    public BibTeXEntry entryToPut(BibTeXEntry entryCandidate, KeyMap<BibTeXEntry> currentEntries) {
        BibTeXEntry existing = currentEntries.get(entryCandidate.getKey());
        if(existing == null) {
            return entryCandidate;
        } else {
            return null;
        }
    }
}
