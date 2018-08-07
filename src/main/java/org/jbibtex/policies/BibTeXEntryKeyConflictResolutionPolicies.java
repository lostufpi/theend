package org.jbibtex.policies;

import org.jbibtex.policies.keyconflictresolution.IgnoreSubsequent;
import org.jbibtex.policies.keyconflictresolution.OverrideWithSubsequent;
import org.jbibtex.policies.keyconflictresolution.RekeySubsequent;
import org.jbibtex.policies.keyconflictresolution.ThrowOnDuplicate;

public class BibTeXEntryKeyConflictResolutionPolicies {
    public static final BibTeXEntryKeyConflictResolutionPolicy IGNORE_SUBSEQUENT = new IgnoreSubsequent();
    public static final BibTeXEntryKeyConflictResolutionPolicy OVERRIDE_WITH_SUBSEQUENT = new OverrideWithSubsequent();
    public static final BibTeXEntryKeyConflictResolutionPolicy REKEY_SUBSEQUENT = new RekeySubsequent();
    public static final BibTeXEntryKeyConflictResolutionPolicy THROW_ON_DUPLICATE = new ThrowOnDuplicate();
}
