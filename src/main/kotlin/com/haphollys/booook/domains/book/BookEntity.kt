package com.haphollys.booook.domains.book

import com.haphollys.booook.domains.room.NoSeatsException
import com.haphollys.booook.domains.room.Seat
import com.haphollys.booook.domains.screen.ScreenEntity
import com.haphollys.booook.domains.user.UserEntity
import javax.persistence.*

@Table(name = "books")
@Entity
class BookEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @OneToOne
    val user: UserEntity,
    @OneToOne
    val screenEntity: ScreenEntity,
    @ElementCollection
    val seats: List<Seat>
) {

    init {
        verifyProps()
    }

    private fun verifyProps() {
        if(seats.isEmpty())
            throw NoSeatsException()
    }

}