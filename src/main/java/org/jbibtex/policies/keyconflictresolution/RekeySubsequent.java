package org.jbibtex.policies.keyconflictresolution;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.KeyMap;
import org.jbibtex.policies.BibTeXEntryKeyConflictResolutionPolicy;

public class RekeySubsequent implements BibTeXEntryKeyConflictResolutionPolicy {
    @Override
    public BibTeXEntry entryToPut(BibTeXEntry entryCandidate, KeyMap<BibTeXEntry> currentEntries) {
        if(!currentEntries.containsKey(entryCandidate.getKey())) {
            return entryCandidate;
        }

        int i = 0;
        Key k = null;
        do {
            k = new Key(String.format("%s-%d", entryCandidate.getKey().getValue(), ++i));
        } while(currentEntries.containsKey(k));

        BibTeXEntry entryToAdd = new BibTeXEntry(entryCandidate.getType(), k);
        entryToAdd.addAllFields(entryCandidate.getFields());
        return entryToAdd;
    }
}
