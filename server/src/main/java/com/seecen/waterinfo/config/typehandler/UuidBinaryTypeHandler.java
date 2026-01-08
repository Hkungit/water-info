package com.seecen.waterinfo.config.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.nio.ByteBuffer;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@MappedTypes(UUID.class)
@MappedJdbcTypes({JdbcType.BINARY, JdbcType.VARBINARY})
public class UuidBinaryTypeHandler extends BaseTypeHandler<UUID> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
        ps.setBytes(i, toBytes(parameter));
    }

    @Override
    public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return toUuid(rs.getBytes(columnName));
    }

    @Override
    public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return toUuid(rs.getBytes(columnIndex));
    }

    @Override
    public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toUuid(cs.getBytes(columnIndex));
    }

    private byte[] toBytes(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    private UUID toUuid(byte[] bytes) {
        if (bytes == null || bytes.length != 16) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        long high = buffer.getLong();
        long low = buffer.getLong();
        return new UUID(high, low);
    }
}
