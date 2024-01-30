package ru.itmo.backend.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import ru.itmo.backend.models.ReactGroup;

import javax.validation.ConstraintViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ReactGroupRepositoryTests {
    @Autowired
    private ReactGroupRepository reactGroupRepository;

    @Test
    public void ReactGroupRepository_SaveReactGroup_ReturnSavedMember(){
        ReactGroup reactGroup = ReactGroup.builder()
                .memberName("John Malkovich")
                .telegramId(15579)
                .build();
        reactGroupRepository.save(reactGroup);
        Assertions.assertNotNull(reactGroup.getId());
        ReactGroup foundMember = reactGroupRepository.findById(reactGroup.getId()).orElse(null);
        Assertions.assertNotNull(foundMember);
        Assertions.assertEquals(reactGroup.getMemberName(), foundMember.getMemberName());
    }

    @Test
    public void ReactGroupRepository_SaveNotValidReactGroup_ThrowsException(){
        ReactGroup reactGroup = ReactGroup.builder()
                .telegramId(15579)
                .build();

        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            reactGroupRepository.save(reactGroup);
        });
    }

    @Test
    public void ReactGroupRepository_FindByTelegramId_ReturnsMember() {
        ReactGroup reactGroup = ReactGroup.builder()
                .memberName("John Malkovich")
                .telegramId(15579)
                .build();
        reactGroupRepository.save(reactGroup);
        ReactGroup foundMember = reactGroupRepository.findByTelegramId(reactGroup.getTelegramId());
        Assertions.assertNotNull(foundMember);
    }

    @Test
    public void ReactGroupRepository_AddNewReactGroupWithSameTelegram_ThrowsError(){
        ReactGroup reactGroup = ReactGroup.builder()
                .memberName("John Malkovich")
                .telegramId(15579)
                .build();
        reactGroupRepository.save(reactGroup);
        ReactGroup reactGroup1 = ReactGroup.builder()
                .memberName("John Malkovich")
                .telegramId(15579)
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            reactGroupRepository.save(reactGroup1);
        });
    }

    @Test
    public void ReactGroupRepository_UpdateReactGroup_ReturnsReactGroupNotNull(){
        ReactGroup reactGroup = ReactGroup.builder()
                .memberName("John Malkovich")
                .telegramId(15579)
                .build();
        reactGroupRepository.save(reactGroup);
        ReactGroup memberToUpdate = reactGroupRepository.findById(reactGroup.getId()).get();
        memberToUpdate.setMemberName("Jack Nollan");
        ReactGroup savedMember = reactGroupRepository.save(memberToUpdate);
        Assertions.assertNotNull(savedMember);
        Assertions.assertEquals("Jack Nollan", savedMember.getMemberName());
    }

    @Test
    public void ReactGroupRepository_DeleteReactGroup_ReturnsNull() {
        ReactGroup reactGroup = ReactGroup.builder()
                .memberName("John Malkovich")
                .telegramId(15579)
                .build();
        reactGroupRepository.save(reactGroup);
        reactGroupRepository.deleteById(reactGroup.getId());
        ReactGroup foundGroup = reactGroupRepository.findById(reactGroup.getId()).orElse(null);
        Assertions.assertNull(foundGroup);
    }
}
