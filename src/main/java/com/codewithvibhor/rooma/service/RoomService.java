package com.codewithvibhor.rooma.service;

import com.codewithvibhor.rooma.exception.ResourceNotFoundException;
import com.codewithvibhor.rooma.model.Room;
import com.codewithvibhor.rooma.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService implements  IRoomService {

private final RoomRepository roomRepository;
    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal roomPrice) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setRoomPrice(roomPrice);
        if(!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);

        }
        return roomRepository.save(room);
    }




    @Override
    public List<String> getAllRoomTypes()
    {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        //I have made changes as I was getting error in optional(),isEmpty(),get()
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if(((Optional<?>) theRoom).isEmpty()){
            throw new ResourceNotFoundException("Sorry,Room not found!");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1,(int)photoBlob.length());
        }
        return null;
    }
}
