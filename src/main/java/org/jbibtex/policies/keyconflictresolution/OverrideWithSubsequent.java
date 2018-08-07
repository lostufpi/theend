package org.jbibtex.policies.keyconflictresolution;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.KeyMap;
import org.jbibtex.policies.BibTeXEntryKeyConflictResolutionPolicy;

public class OverrideWithSubsequent implements BibTeXEntryKeyConflictResolutionPolicy {
    @Override
    public BibTeXEntry entryToPut(BibTeXEntry entryCandidate, KeyMap<BibTeXEntry> currentEntries) {
        return entryCandidate;
    }
}
