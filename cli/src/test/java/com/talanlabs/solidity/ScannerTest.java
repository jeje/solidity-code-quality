package com.talanlabs.solidity;

import com.talanlabs.solidity.model.ValidationResults;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ScannerTest {

    @Test
    public void test_scanner_on_contract_having_multiple_errors_across_multiple_rules() throws IOException {
        Scanner scanner = new Scanner();
        ValidationResults results = scanner.scan("src/test/resources/contracts/MultipleErrorsContract.sol");
        assertEquals(2, results.getErrors().size());
    }

}
