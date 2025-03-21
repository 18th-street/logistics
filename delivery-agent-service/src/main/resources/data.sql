INSERT INTO public.p_delivery_agent
(delivery_agent_id, created_at, created_by, deleted_at, is_deleted, modified_at, modified_by, assigned_at, status, type,
 delivery_id, delivery_route_id, hub_id, sequence, slack_id, user_id)
VALUES
-- COMPANY_AGENT 5개
('1a2b3c4d-5678-467a-9101-314159265358', '2025-03-21 11:35:00.000000', null, null, false, '2025-03-21 11:35:00.000000',
 null, null, 'AVAILABLE', 'COMPANY_AGENT', null, null, 'a1b2c3d4-e5f6-467a-8901-56789abcdef0', null,
 'exampleSlackId001', '123e4567-e89b-12d3-a456-426614174000'),
('2b3c4d5e-6789-467a-1011-415926535897', '2025-03-21 11:35:01.000000', null, null, false, '2025-03-21 11:35:01.000000',
 null, null, 'AVAILABLE', 'COMPANY_AGENT', null, null, 'b2c3d4e5-f678-467a-2345-789abcdef123', null,
 'exampleSlackId002', '234e5678-e89b-12d3-a456-426614174001'),
('3c4d5e6f-7890-467a-1121-592653589793', '2025-03-21 11:35:02.000000', null, null, false, '2025-03-21 11:35:02.000000',
 null, null, 'AVAILABLE', 'COMPANY_AGENT', null, null, 'c3d4e5f6-7890-467a-3456-9abcdef23456', null,
 'exampleSlackId003', '345e6789-e89b-12d3-a456-426614174002'),
('4d5e6f7a-8901-467a-1314-159265358979', '2025-03-21 11:35:03.000000', null, null, false, '2025-03-21 11:35:03.000000',
 null, null, 'AVAILABLE', 'COMPANY_AGENT', null, null, 'd4e5f678-9012-467a-5678-abcdef345678', null,
 'exampleSlackId004', '456e7890-e89b-12d3-a456-426614174003'),
('5e6f7a8b-9012-467a-1516-265358979323', '2025-03-21 11:35:04.000000', null, null, false, '2025-03-21 11:35:04.000000',
 null, null, 'AVAILABLE', 'COMPANY_AGENT', null, null, 'e5f67890-1234-467a-7890-bcdef4567890', null,
 'exampleSlackId005', '567e8901-e89b-12d3-a456-426614174004'),

-- HUB_AGENT 5개
('aa1b2c3d-5678-467a-9101-314159265358', '2025-03-21 11:36:00.000000', null, null, false, '2025-03-21 11:36:00.000000',
 null, null, 'AVAILABLE', 'HUB_AGENT', null, null, 'ba2c3d4e-5f67-467a-2345-6789abcdef00', null, 'exampleSlackId011',
 '112e3456-e89b-12d3-a456-426614174010'),
('bb2c3d4e-6789-467a-1011-415926535897', '2025-03-21 11:36:01.000000', null, null, false, '2025-03-21 11:36:01.000000',
 null, null, 'AVAILABLE', 'HUB_AGENT', null, null, 'cb3d4e5f-6789-467a-4567-89abcdef0112', null, 'exampleSlackId012',
 '223e4567-e89b-12d3-a456-426614174011'),
('cc3d4e5f-7890-467a-1121-592653589793', '2025-03-21 11:36:02.000000', null, null, false, '2025-03-21 11:36:02.000000',
 null, null, 'AVAILABLE', 'HUB_AGENT', null, null, 'dc3d4e5f-6789-467a-6789-abcdef112233', null, 'exampleSlackId013',
 '334e5678-e89b-12d3-a456-426614174012'),
('dd4e5f6a-8901-467a-1314-159265358979', '2025-03-21 11:36:03.000000', null, null, false, '2025-03-21 11:36:03.000000',
 null, null, 'AVAILABLE', 'HUB_AGENT', null, null, 'ed4e5f6a-9012-467a-7890-bcdef3344556', null, 'exampleSlackId014',
 '445e6789-e89b-12d3-a456-426614174013'),
('ee5f6a7b-9012-467a-1516-265358979323', '2025-03-21 11:36:04.000000', null, null, false, '2025-03-21 11:36:04.000000',
 null, null, 'AVAILABLE', 'HUB_AGENT', null, null, 'fd5f6a7b-9012-467a-8901-cdef44556677', null, 'exampleSlackId015',
 '556e7890-e89b-12d3-a456-426614174014');