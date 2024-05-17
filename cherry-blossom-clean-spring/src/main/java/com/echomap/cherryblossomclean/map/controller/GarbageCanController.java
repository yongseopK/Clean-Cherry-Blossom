package com.echomap.cherryblossomclean.map.controller;

import com.echomap.cherryblossomclean.map.service.GarbageCanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GarbageCanController {

    private final GarbageCanService garbageCanService;


}
