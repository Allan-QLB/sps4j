package com.github.sps4j.annotation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PluginProcessorTest {

    @InjectMocks
    private PluginProcessor processor;

    @Mock
    private ProcessingEnvironment processingEnv;
    @Mock
    private Types typeUtils;

    @Mock
    private TypeElement classElement;
    @Mock
    private TypeElement parentInterfaceElement;
    @Mock
    private TypeElement grandParentInterfaceElement;
    @Mock
    private DeclaredType parentInterfaceMirror;
    @Mock
    private DeclaredType grandParentInterfaceMirror;


    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void testFindAllInterfacesOfType() {
        // --- MOCK SETUP ---
        processor.init(processingEnv);
        when(processingEnv.getTypeUtils()).thenReturn(typeUtils);

        // 1. Define the hierarchy:
        //    - GrandParentInterface (no super-interfaces)
        //    - ParentInterface extends GrandParentInterface
        //    - MyClass implements ParentInterface

        // Use raw List to bypass generics issue
        List parentInterfaces = Collections.singletonList(parentInterfaceMirror);
        List grandParentInterfaces = Collections.singletonList(grandParentInterfaceMirror);


        // 2. Mock behavior for MyClass
        when(classElement.getInterfaces()).thenReturn(parentInterfaces);

        // 3. Mock behavior for ParentInterface
        when(parentInterfaceMirror.toString()).thenReturn("com.example.ParentInterface");
        when(typeUtils.asElement(parentInterfaceMirror)).thenReturn(parentInterfaceElement);
        when(parentInterfaceElement.getInterfaces()).thenReturn(grandParentInterfaces);

        // 4. Mock behavior for GrandParentInterface
        when(grandParentInterfaceMirror.toString()).thenReturn("com.example.GrandParentInterface");
        when(typeUtils.asElement(grandParentInterfaceMirror)).thenReturn(grandParentInterfaceElement);
        when(grandParentInterfaceElement.getInterfaces()).thenReturn(Collections.emptyList());


        // --- EXECUTION ---
        Map<String, TypeElement> interfaces = processor.findAllInterfacesOfType(classElement);


        // --- VERIFICATION ---
        assertNotNull(interfaces);
        assertEquals(2, interfaces.size());
        assertTrue(interfaces.containsKey("com.example.ParentInterface"));
        assertTrue(interfaces.containsKey("com.example.GrandParentInterface"));
        assertEquals(parentInterfaceElement, interfaces.get("com.example.ParentInterface"));
        assertEquals(grandParentInterfaceElement, interfaces.get("com.example.GrandParentInterface"));
    }

    @Test
    void testIsValidVersionConstraint() {
        assertTrue(PluginProcessor.isValidVersionConstraint(">=1.0.0"));
        assertTrue(PluginProcessor.isValidVersionConstraint("~1.2.3"));
        assertFalse(PluginProcessor.isValidVersionConstraint("1.0.0-invalid"));
        assertFalse(PluginProcessor.isValidVersionConstraint(""));
    }
}