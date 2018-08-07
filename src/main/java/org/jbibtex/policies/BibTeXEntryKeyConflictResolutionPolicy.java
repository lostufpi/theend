package org.jbibtex.policies;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.KeyMap;

import java.io.Serializable;

public interface BibTeXEntryKeyConflictResolutionPolicy extends Serializable {
    BibTeXEntry entryToPut(BibTeXEntry entryCandidate, KeyMap<BibTeXEntry> currentEntries);
}
