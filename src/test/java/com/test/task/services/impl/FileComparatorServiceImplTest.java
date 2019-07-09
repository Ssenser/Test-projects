package com.test.task.services.impl;

import com.test.task.converter.Converter;
import com.test.task.dto.FileLine;
import com.test.task.dto.FileTuple;
import com.test.task.dto.FilesCompareResult;
import com.test.task.services.LineReaderWrapperService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.collections.Sets;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.LineNumberReader;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileComparatorServiceImplTest {

    private static final String FILE_NAME_1 = "file1";
    private static final String FILE_NAME_2 = "file2";
    private static final String NOT_EMPTY_STRING_1 = "123123123 123123";
    private static final String NOT_EMPTY_STRING_2 = "232324 24234";

    private static final String UUID_1 = "d14e0241-bda9-4a7f-8553-230a96dc7cb8";
    private static final String UUID_2 = "d14e0241-bda9-4a7f-8553-230a96dc7cb9";
    private static final Long EVENT_ID_1 = 201L;
    private static final Long EVENT_ID_2 = 202L;
    private static final Long EVENT_ID_3 = 203L;
    private static final Long DATE_CREATED_1 = 1487589782000L;
    private static final Long DATE_CREATED_2 = 1487600499000L;
    private static final Long DATE_CREATED_3 = 1487604094000L;
    private static final String HOST_1 = "null";
    private static final String HOST_2 = "app74.test.com";
    private static final String HOST_3 = "app69.test.com";

    @Mock
    private LineNumberReader firstReader;

    @Mock
    private LineNumberReader secondReader;

    @Mock
    private LineReaderWrapperService wrapperService;

    @Mock
    private Converter<String, FileLine> converter;

    @InjectMocks
    private FileComparatorServiceImpl testingInstance;

    @Before
    public void setUp() {
        when(wrapperService.getReader(FILE_NAME_1)).thenReturn(firstReader);
        when(wrapperService.getReader(FILE_NAME_2)).thenReturn(secondReader);
    }

    @Test
    public void shouldExitOnEmptyFile() {
        FilesCompareResult expectedResult = new FilesCompareResult();

        when(wrapperService.readLine(firstReader)).thenReturn(null);
        when(wrapperService.readLine(secondReader)).thenReturn(null);

        final FilesCompareResult actualResult = testingInstance.compareFiles(FILE_NAME_1, FILE_NAME_2);

        verify(wrapperService).getReader(FILE_NAME_1);
        verify(wrapperService).getReader(FILE_NAME_2);
        verify(wrapperService, times(1)).readLine(firstReader);
        verify(wrapperService, times(1)).readLine(secondReader);

        verifyZeroInteractions(converter);

        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void shouldCompareFilesWithSameId() {
        FileTuple tuple1 = new FileTuple(EVENT_ID_1, DATE_CREATED_1, HOST_1);
        FileTuple tuple2 = new FileTuple(EVENT_ID_2, DATE_CREATED_2, HOST_2);
        FileTuple tuple3 = new FileTuple(EVENT_ID_3, DATE_CREATED_3, HOST_3);
        FileLine fileLine1 = new FileLine(UUID_1, Sets.newSet(tuple1, tuple2));
        FileLine fileLine2 = new FileLine(UUID_1, Sets.newSet(tuple1, tuple3));
        FilesCompareResult expectedResult = new FilesCompareResult();
        expectedResult.getUuidToFirstFileUniqueTuples().put(UUID_1, Sets.newSet(tuple2));
        expectedResult.getUuidToSecondFileUniqueTuples().put(UUID_1, Sets.newSet(tuple3));

        doAnswer(getFirstReadLineAnswerForOneInvocation()).when(wrapperService).readLine(firstReader);
        doAnswer(getSecondReadLineAnswerForOneInvocation()).when(wrapperService).readLine(secondReader);

        when(converter.convert(NOT_EMPTY_STRING_1)).thenReturn(fileLine1);
        when(converter.convert(NOT_EMPTY_STRING_2)).thenReturn(fileLine2);

        final FilesCompareResult actualResult = testingInstance.compareFiles(FILE_NAME_1, FILE_NAME_2);

        verify(wrapperService).getReader(FILE_NAME_1);
        verify(wrapperService).getReader(FILE_NAME_2);
        verify(wrapperService, times(2)).readLine(firstReader);
        verify(wrapperService, times(2)).readLine(secondReader);
        verify(wrapperService).close(firstReader);
        verify(wrapperService).close(secondReader);
        verify(converter).convert(NOT_EMPTY_STRING_1);
        verify(converter).convert(NOT_EMPTY_STRING_2);

        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void shouldCompareFilesWithDifferentIdSecondGreater() {
        FileLine fileLine1 = new FileLine(UUID_1, Collections.emptySet());
        FileLine fileLine2 = new FileLine(UUID_2, Collections.emptySet());
        FilesCompareResult expectedResult = new FilesCompareResult();
        expectedResult.getSecondFileOnlyUuids().add(UUID_2);
        expectedResult.getFirstFileOnlyUuids().add(UUID_1);

        when(converter.convert(NOT_EMPTY_STRING_1)).thenReturn(fileLine1);
        when(converter.convert(NOT_EMPTY_STRING_2)).thenReturn(fileLine2);

        doAnswer(getFirstReadLineAnswerForOneInvocation()).when(wrapperService).readLine(firstReader);
        doAnswer(getSecondReadLineAnswerForOneInvocation()).when(wrapperService).readLine(secondReader);

        final FilesCompareResult actualResult = testingInstance.compareFiles(FILE_NAME_1, FILE_NAME_2);

        verify(wrapperService).getReader(FILE_NAME_1);
        verify(wrapperService).getReader(FILE_NAME_2);
        verify(wrapperService, times(2)).readLine(firstReader);
        verify(wrapperService, times(2)).readLine(secondReader);
        verify(wrapperService).close(firstReader);
        verify(wrapperService).close(secondReader);
        verify(converter).convert(NOT_EMPTY_STRING_1);
        verify(converter, times(2)).convert(NOT_EMPTY_STRING_2);

        assertThat(actualResult, is(expectedResult));
    }

    @Test
    public void shouldCompareFilesWithDifferentIdFirstGreater() {
        FileLine fileLine1 = new FileLine(UUID_2, Collections.emptySet());
        FileLine fileLine2 = new FileLine(UUID_1, Collections.emptySet());
        FilesCompareResult expectedResult = new FilesCompareResult();
        expectedResult.getFirstFileOnlyUuids().add(UUID_2);
        expectedResult.getSecondFileOnlyUuids().add(UUID_1);

        when(converter.convert(NOT_EMPTY_STRING_1)).thenReturn(fileLine1);
        when(converter.convert(NOT_EMPTY_STRING_2)).thenReturn(fileLine2);

        doAnswer(getFirstReadLineAnswerForOneInvocation()).when(wrapperService).readLine(firstReader);
        doAnswer(getSecondReadLineAnswerForOneInvocation()).when(wrapperService).readLine(secondReader);

        final FilesCompareResult actualResult = testingInstance.compareFiles(FILE_NAME_1, FILE_NAME_2);

        verify(wrapperService).getReader(FILE_NAME_1);
        verify(wrapperService).getReader(FILE_NAME_2);
        verify(wrapperService, times(2)).readLine(firstReader);
        verify(wrapperService, times(2)).readLine(secondReader);
        verify(wrapperService).close(firstReader);
        verify(wrapperService).close(secondReader);
        verify(converter, times(2)).convert(NOT_EMPTY_STRING_1);
        verify(converter).convert(NOT_EMPTY_STRING_2);

        assertThat(actualResult, is(expectedResult));
    }

    private Answer getFirstReadLineAnswerForOneInvocation() {
        return new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                return count++ == 0 ? NOT_EMPTY_STRING_1 : null;
            }
        };
    }

    private Answer getSecondReadLineAnswerForOneInvocation() {
        return new Answer() {
            private int count = 0;

            public Object answer(InvocationOnMock invocation) {
                return count++ == 0 ? NOT_EMPTY_STRING_2 : null;
            }
        };
    }
}
