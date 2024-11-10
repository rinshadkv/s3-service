package com.assignment.s3;

import com.assignment.s3.Dto.FileInfo;
import com.assignment.s3.controller.S3Controller;
import com.assignment.s3.service.S3Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(S3Controller.class)
class S3ServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSearchFiles() throws Exception {

        String userName = "testUser";
        String query = "document";
        List<FileInfo> mockFiles = new ArrayList<>();
        mockFiles.add(new FileInfo("testUser/document1.pdf", "https://mock-url.com/document1.pdf"));
        mockFiles.add(new FileInfo("testUser/document2.txt", "https://mock-url.com/document2.txt"));

        when(s3Service.searchFilesInUserFolder(anyString(), anyString())).thenReturn(mockFiles);


        mockMvc.perform(get("/api/s3/search")
                        .param("userName", userName)
                        .param("q", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fileKey").value("testUser/document1.pdf"))
                .andExpect(jsonPath("$[0].downloadUrl").value("https://mock-url.com/document1.pdf"))
                .andExpect(jsonPath("$[1].fileKey").value("testUser/document2.txt"))
                .andExpect(jsonPath("$[1].downloadUrl").value("https://mock-url.com/document2.txt"));

        verify(s3Service, times(1)).searchFilesInUserFolder(userName, query);
    }

    @Test
    void testUploadFile() throws Exception {

        String userName = "testUser";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "testFile.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "sample content".getBytes()
        );

        doNothing().when(s3Service).uploadFile(anyString(), anyString(), any());


        mockMvc.perform(multipart("/api/s3/upload/{userName}", userName)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("File uploaded successfully"));

        verify(s3Service, times(1)).uploadFile(eq(userName), eq("testFile.txt"), any());
    }
}
