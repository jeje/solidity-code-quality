package com.talanlabs.solidity;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.internal.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import static com.talanlabs.solidity.SolidityRulesDefinition.REPOSITORY_KEY;
import static org.junit.Assert.assertEquals;

public class SoliditySensorTest {
    private SensorContextTester context;

    @Before
    public void setup_sensor_context() throws IOException {
        Path moduleBaseDir = Paths.get("src/test/resources/contracts").toAbsolutePath();
        context = SensorContextTester.create(moduleBaseDir);
        File contractFile = new File("src/test/resources/contracts/MultipleErrorsContract.sol").getAbsoluteFile();
        DefaultInputFile file = TestInputFileBuilder.create("",
                context.fileSystem().baseDir(),
                contractFile)
                .initMetadata(Files.toString(contractFile, Charset.defaultCharset()))
                .build();
        context.fileSystem().add(file);
        context.fileSystem().inputFiles().forEach(f -> {});
    }

    @Test
    public void test_sensor() {
        SoliditySensor sensor = new SoliditySensor();
        DefaultSensorDescriptor sensorDescriptor = new DefaultSensorDescriptor();
        // ensure the sensor descriptor is properly defined
        sensor.describe(sensorDescriptor);
        assertEquals(SoliditySensor.NAME, sensorDescriptor.name());
        Collection<String> repositories = sensorDescriptor.ruleRepositories();
        assertEquals(1, repositories.size());
        assertEquals(REPOSITORY_KEY, repositories.iterator().next());
        // ensure that rules run properly
        sensor.execute(context);
        assertEquals(2, context.allIssues().size());
    }

}
